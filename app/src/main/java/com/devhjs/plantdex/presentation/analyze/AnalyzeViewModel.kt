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

    private var analyzeJob: Job? = null
    private var registerJob: Job? = null

    init {
        analyze()
    }

    fun onAction(action: AnalyzeAction) {
        when (action) {
            AnalyzeAction.Analyze -> analyze()
            AnalyzeAction.Register -> register()
            AnalyzeAction.Retake -> Unit // Root 가 처리한다
        }
    }

    private fun analyze() {
        if (analyzeJob?.isActive == true) return

        analyzeJob = viewModelScope.launch {
            _state.value = AnalyzeState.Loading
            _state.value = when (val result = analyzePlantPhoto(dummyPhoto())) {
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
            val entry = registerDexEntry(success.plant)
            _event.emit(AnalyzeEvent.Registered(entry.id))
        }
    }

    /** TODO: CameraX 촬영 화면이 붙으면 화면이 PlantPhoto 를 넘겨주고 이 함수는 삭제한다. */
    private fun dummyPhoto() = PlantPhoto(ByteArray(DUMMY_PHOTO_SIZE) { it.toByte() })

    private companion object {
        const val DUMMY_PHOTO_SIZE = 1024
    }
}
