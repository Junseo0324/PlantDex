package com.devhjs.plantdex.presentation.camera

/** ratio 는 CameraX 의 setZoomRatio 에 그대로 넘긴다. */
enum class CameraZoom(val label: String, val ratio: Float) {
    X1("1x", 1f),
    X2("2x", 2f),
    X5("5x", 5f),
}
