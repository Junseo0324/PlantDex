package com.devhjs.plantdex.presentation.camera

enum class CameraLens {
    BACK,
    FRONT;

    fun flipped(): CameraLens = if (this == BACK) FRONT else BACK
}
