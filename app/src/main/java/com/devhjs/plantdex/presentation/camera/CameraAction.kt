package com.devhjs.plantdex.presentation.camera

import androidx.compose.ui.geometry.Offset

sealed interface CameraAction {
    data class SelectZoom(val zoom: CameraZoom) : CameraAction
    data object ToggleFlash : CameraAction
    data object ToggleLens : CameraAction
    data object Shutter : CameraAction

    /** Root 가 CameraX 촬영을 마치고 되돌려준다. 실패하면 null 이다. */
    data class Captured(val photoUri: String?) : CameraAction

    data class FocusAt(val offset: Offset) : CameraAction
    data object Close : CameraAction
}
