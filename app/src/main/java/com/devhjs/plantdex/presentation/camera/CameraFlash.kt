package com.devhjs.plantdex.presentation.camera

enum class CameraFlash {
    AUTO,
    ON,
    OFF;

    fun next(): CameraFlash = entries[(ordinal + 1) % entries.size]
}
