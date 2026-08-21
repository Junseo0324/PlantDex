package com.devhjs.plantdex.presentation.camera

import androidx.compose.ui.geometry.Offset

sealed interface CameraAction {
    data class SelectZoom(val zoom: CameraZoom) : CameraAction
    data object ToggleFlash : CameraAction
    data object ToggleLens : CameraAction
    data object Shutter : CameraAction

    /** Root 가 CameraX 촬영을 마치고 되돌려준다. 실패하면 null 이다. */
    data class Captured(val photoUri: String?) : CameraAction

    data object PickFromGallery : CameraAction

    /** 갤러리에서 고른 위치. 그대로는 오래 못 쓰므로 앱 저장소로 복사한다. */
    data class PickedFromGallery(val contentUri: String) : CameraAction

    data class FocusAt(val offset: Offset) : CameraAction
    data object Close : CameraAction
}
