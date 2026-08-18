package com.devhjs.plantdex.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.core.util.Result
import com.devhjs.plantdex.domain.model.PlantPhoto
import com.devhjs.plantdex.domain.usecase.AnalyzePlantPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    private val analyzePlantPhoto: AnalyzePlantPhotoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AnalyzeState>(AnalyzeState.Loading)
    val state: StateFlow<AnalyzeState> = _state.asStateFlow()

    private var analyzeJob: Job? = null

    init {
        analyze()
    }

    fun onAction(action: AnalyzeAction) {
        when (action) {
            AnalyzeAction.Analyze -> analyze()
        }
    }

    private fun analyze() {
        if (analyzeJob?.isActive == true) return

        analyzeJob = viewModelScope.launch {
            _state.value = AnalyzeState.Loading
            _state.value = when (val result = analyzePlantPhoto(dummyPhoto())) {
                is Result.Success -> AnalyzeState.Success(result.data)
                is Result.Error -> AnalyzeState.Error(result.error)
            }
        }
    }

    /** TODO: CameraX 촬영 화면이 붙으면 화면이 PlantPhoto 를 넘겨주고 이 함수는 삭제한다. */
    private fun dummyPhoto() = PlantPhoto(ByteArray(DUMMY_PHOTO_SIZE) { it.toByte() })

    private companion object {
        const val DUMMY_PHOTO_SIZE = 1024
    }
}
