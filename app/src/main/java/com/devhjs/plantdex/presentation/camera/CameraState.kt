package com.devhjs.plantdex.presentation.camera

data class CameraState(
    val zoom: CameraZoom = CameraZoom.X1,
    val flash: CameraFlash = CameraFlash.AUTO,
    val lens: CameraLens = CameraLens.BACK,
    val isCapturing: Boolean = false,
)
