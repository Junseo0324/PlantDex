package com.devhjs.plantdex.presentation.camera

import androidx.compose.ui.geometry.Offset

sealed interface CameraEvent {
    /** 하드웨어를 쥔 Root 가 촬영하고 결과를 [CameraAction.Captured] 로 되돌린다. */
    data object RequestCapture : CameraEvent
    data class RequestFocus(val offset: Offset) : CameraEvent
    data object RequestGallery : CameraEvent

    data class Captured(val photoUri: String) : CameraEvent
    data object CaptureFailed : CameraEvent
    data object Close : CameraEvent
}
