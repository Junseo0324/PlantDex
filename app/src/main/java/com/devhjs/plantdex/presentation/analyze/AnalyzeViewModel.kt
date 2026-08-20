package com.devhjs.plantdex.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.model.PlantPhoto
import com.devhjs.plantdex.domain.usecase.AnalyzePlantPhotoUseCase
import com.devhjs.plantdex.domain.usecase.GetNextDexNumberUseCase
import com.devhjs.plantdex.domain.usecase.RegisterDexEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    private val analyzePlantPhoto: AnalyzePlantPhotoUseCase,
    private val getNextDexNumber: GetNextDexNumberUseCase,
    private val registerDexEntry: RegisterDexEntryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AnalyzeState>(AnalyzeState.Loading)
    val state: StateFlow<AnalyzeState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AnalyzeEvent>()
    val event = _event.asSharedFlow()

    private var photoUri: String? = null
    private var isStarted = false
    private var analyzeJob: Job? = null
    private var registerJob: Job? = null

    fun onAction(action: AnalyzeAction) {
        when (action) {
            is AnalyzeAction.Start -> start(action.photoUri)
            AnalyzeAction.Retry -> analyze()
            AnalyzeAction.Register -> register()
            AnalyzeAction.Retake -> viewModelScope.launch { _event.emit(AnalyzeEvent.Retake) }
        }
    }

    /** Root 가 화면에 다시 들어올 때마다 보내므로 첫 번째만 받는다. */
    private fun start(photoUri: String?) {
        if (isStarted) return
        isStarted = true
        this.photoUri = photoUri
        analyze()
    }

    private fun analyze() {
        if (analyzeJob?.isActive == true) return

        analyzeJob = viewModelScope.launch {
            _state.value = AnalyzeState.Loading
            _state.value = when (val result = analyzePlantPhoto(loadPhoto())) {
                // 아직 등록 전이라 "등록하면 받게 될" 번호를 미리 읽어둔다.
                is Result.Success -> AnalyzeState.Success(result.data, getNextDexNumber())
                is Result.Error -> AnalyzeState.Error(result.error)
            }
        }
    }

    private fun register() {
        val success = _state.value as? AnalyzeState.Success ?: return
        if (registerJob?.isActive == true) return

        registerJob = viewModelScope.launch {
            val entry = registerDexEntry(success.plant, photoUri)
            _event.emit(AnalyzeEvent.Registered(entry.id))
        }
    }

    /** TODO: CameraX 가 붙으면 photoUri 의 파일을 읽어 PlantPhoto 를 만든다. */
    private fun loadPhoto() = PlantPhoto(ByteArray(DUMMY_PHOTO_SIZE) { it.toByte() })

    private companion object {
        const val DUMMY_PHOTO_SIZE = 1024
    }
}
