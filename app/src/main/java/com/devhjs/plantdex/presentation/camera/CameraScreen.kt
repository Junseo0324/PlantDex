package com.devhjs.plantdex.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.component.AppIconButton
import com.devhjs.plantdex.presentation.component.CameraBottomBar
import com.devhjs.plantdex.presentation.component.CameraFocusOverlay
import com.devhjs.plantdex.presentation.component.CameraPreviewPlaceholder
import com.devhjs.plantdex.presentation.component.FilterChip
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.text.labelRes

/**
 * [previewContent] 는 CameraX 프리뷰 자리다. @Preview 는 카메라를 열 수 없어 기본값을 쓴다.
 */
@Composable
fun CameraScreen(
    state: CameraState,
    onAction: (CameraAction) -> Unit,
    modifier: Modifier = Modifier,
    previewContent: @Composable BoxScope.() -> Unit = { CameraPreviewPlaceholder() },
) {
    // 배경은 상태바 뒤까지 깔고 콘텐츠에만 인셋을 준다.
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Charcoal),
    ) {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.screenH, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppIconButton(
                    iconRes = R.drawable.ic_close,
                    contentDescription = stringResource(R.string.camera_close),
                    onClick = { onAction(CameraAction.Close) },
                    containerColor = AppColors.OnDark.copy(alpha = 0.14f),
                    tint = AppColors.OnDark,
                )
                // 아이콘은 한 종류뿐이라 라벨로만 상태를 보인다.
                FilterChip(
                    label = stringResource(state.flash.labelRes()),
                    isSelected = state.flash != CameraFlash.AUTO,
                    onClick = { onAction(CameraAction.ToggleFlash) },
                    selectedContainerColor = AppColors.Cream,
                    selectedContentColor = AppColors.Charcoal,
                    unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                    unselectedContentColor = AppColors.OnDark,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_flash),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = if (state.flash == CameraFlash.AUTO) {
                                AppColors.OnDark
                            } else {
                                AppColors.Charcoal
                            },
                        )
                    },
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(AppRadii.hero))
                    .pointerInput(Unit) {
                        detectTapGestures { offset -> onAction(CameraAction.FocusAt(offset)) }
                    },
            ) {
                previewContent()

                Text(
                    text = stringResource(R.string.camera_guide),
                    style = AppTextStyles.Body.copy(color = AppColors.OnDarkMuted),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 22.dp),
                )

                CameraFocusOverlay(modifier = Modifier.align(Alignment.Center))

                Text(
                    text = stringResource(R.string.camera_preview_caption),
                    style = AppTextStyles.Code.copy(color = AppColors.OnDarkFaint),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            ) {
                CameraZoom.entries.forEach { entry ->
                    FilterChip(
                        label = entry.label,
                        isSelected = entry == state.zoom,
                        onClick = { onAction(CameraAction.SelectZoom(entry)) },
                        selectedContainerColor = AppColors.Cream,
                        selectedContentColor = AppColors.Charcoal,
                        unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                        unselectedContentColor = AppColors.OnDarkMuted,
                    )
                }
            }

            CameraBottomBar(
                onShutter = { onAction(CameraAction.Shutter) },
                onFlipLens = { onAction(CameraAction.ToggleLens) },
                shutterContentDescription = stringResource(R.string.camera_shutter),
                flipContentDescription = stringResource(R.string.camera_flip_lens),
                modifier = Modifier.padding(start = 44.dp, end = 44.dp, top = 26.dp, bottom = 44.dp),
                isShutterEnabled = !state.isCapturing,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenPreview() {
    PlantDexTheme {
        CameraScreen(state = CameraState(), onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenZoomedPreview() {
    PlantDexTheme {
        CameraScreen(state = CameraState(zoom = CameraZoom.X2), onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenFlashOnPreview() {
    PlantDexTheme {
        CameraScreen(state = CameraState(flash = CameraFlash.ON), onAction = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenCapturingPreview() {
    PlantDexTheme {
        CameraScreen(state = CameraState(isCapturing = true), onAction = {})
    }
}
