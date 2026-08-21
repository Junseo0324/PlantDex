package com.devhjs.plantdex.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.usecase.AnalyzePlantPhotoUseCase
import com.devhjs.plantdex.domain.usecase.DeletePhotoUseCase
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
    private val deletePhoto: DeletePhotoUseCase,
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
            AnalyzeAction.Retake -> retake()
        }
    }

    /** 등록하지 않고 나가므로 방금 찍은 사진이 남지 않게 지운다. */
    private fun retake() {
        viewModelScope.launch {
            deletePhoto(photoUri)
            _event.emit(AnalyzeEvent.Retake)
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
            _state.value = when (val result = analyzePlantPhoto(photoUri)) {
                // 아직 등록 전이라 "등록하면 받게 될" 번호를 미리 읽어둔다.
                is Result.Success ->
                    AnalyzeState.Success(result.data, getNextDexNumber(), photoUri)
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
}
