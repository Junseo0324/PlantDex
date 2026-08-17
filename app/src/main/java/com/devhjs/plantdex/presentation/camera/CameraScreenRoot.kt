package com.devhjs.plantdex.presentation.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * 아직 ViewModel 이 없다. 줌 선택은 화면 밖으로 나갈 일이 없어 여기서 들고 있고,
 * 실제 CameraX 가 붙으면 ViewModel 로 옮긴다.
 */
@Composable
fun CameraScreenRoot(
    onShutter: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var zoom by rememberSaveable { mutableStateOf(CameraZoom.X1) }

    CameraScreen(
        zoom = zoom,
        onZoomChange = { zoom = it },
        onShutter = onShutter,
        onClose = onClose,
        modifier = modifier,
    )
}
