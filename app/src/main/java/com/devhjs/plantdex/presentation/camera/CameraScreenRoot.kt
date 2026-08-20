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
    onCaptured: (photoUri: String?) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var zoom by rememberSaveable { mutableStateOf(CameraZoom.X1) }

    CameraScreen(
        zoom = zoom,
        onZoomChange = { zoom = it },
        // TODO: CameraX 가 붙으면 촬영 결과 파일의 uri 를 넘긴다. 그때까지는 사진이 없다.
        onShutter = { onCaptured(null) },
        onClose = onClose,
        modifier = modifier,
    )
}
