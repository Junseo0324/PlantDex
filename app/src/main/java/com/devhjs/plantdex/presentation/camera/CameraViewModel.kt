package com.devhjs.plantdex.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.SavePickedPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 카메라 하드웨어는 Root 가 쥔다. 여기서는 UI 상태와 셔터 연타만 관리한다.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePickedPhoto: SavePickedPhotoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<CameraEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CameraAction) {
        when (action) {
            is CameraAction.SelectZoom -> _state.update { it.copy(zoom = action.zoom) }
            CameraAction.ToggleFlash -> _state.update { it.copy(flash = it.flash.next()) }
            CameraAction.ToggleLens -> _state.update { it.copy(lens = it.lens.flipped()) }
            CameraAction.Shutter -> shutter()
            is CameraAction.Captured -> captured(action.photoUri)
            CameraAction.PickFromGallery -> emit(CameraEvent.RequestGallery)
            is CameraAction.PickedFromGallery -> savePicked(action.contentUri)
            is CameraAction.FocusAt -> emit(CameraEvent.RequestFocus(action.offset))
            CameraAction.Close -> emit(CameraEvent.Close)
        }
    }

    private fun shutter() {
        if (_state.value.isCapturing) return
        _state.update { it.copy(isCapturing = true) }
        emit(CameraEvent.RequestCapture)
    }

    /** 촬영을 요청한 적이 없으면 들어온 결과를 버린다. */
    private fun captured(photoUri: String?) {
        if (!_state.value.isCapturing) return
        _state.update { it.copy(isCapturing = false) }
        emit(if (photoUri == null) CameraEvent.CaptureFailed else CameraEvent.Captured(photoUri))
    }

    private fun savePicked(contentUri: String) {
        if (_state.value.isCapturing) return
        _state.update { it.copy(isCapturing = true) }

        viewModelScope.launch {
            val saved = savePickedPhoto(contentUri)
            _state.update { it.copy(isCapturing = false) }
            _event.emit(
                if (saved == null) CameraEvent.CaptureFailed else CameraEvent.Captured(saved),
            )
        }
    }

    private fun emit(event: CameraEvent) {
        viewModelScope.launch { _event.emit(event) }
    }
}
