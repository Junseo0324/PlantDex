package com.devhjs.plantdex.presentation.camera

/** 실제 확대는 CameraX 가 붙을 때 연결한다. 지금은 칩 선택 표시만 한다. */
enum class CameraZoom(val label: String) {
    X1("1x"),
    X2("2x"),
    X5("5x"),
}
