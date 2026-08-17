package com.devhjs.plantdex.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.component.CameraBottomBar
import com.devhjs.plantdex.presentation.component.CameraFocusOverlay
import com.devhjs.plantdex.presentation.component.FilterChip
import com.devhjs.plantdex.presentation.component.StripeSurface
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

@Composable
fun CameraScreen(
    zoom: CameraZoom,
    onZoomChange: (CameraZoom) -> Unit,
    onShutter: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
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
                    .padding(horizontal = AppSpacing.screenH, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.camera_close),
                    style = AppTextStyles.Body.copy(
                        fontSize = 15.sp,
                        color = AppColors.OnDarkMuted,
                    ),
                    modifier = Modifier.clickable(onClick = onClose),
                )
                // 디자인이 "자동" 고정 표시라 토글하지 않는다.
                FilterChip(
                    label = stringResource(R.string.camera_flash_auto),
                    isSelected = false,
                    onClick = {},
                    unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                    unselectedContentColor = AppColors.OnDark,
                )
            }

            StripeSurface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(AppRadii.hero),
                baseColor = AppColors.CharcoalRaise,
                stripeColor = AppColors.CharcoalRaise2,
            ) {
                Text(
                    text = stringResource(R.string.camera_guide),
                    style = AppTextStyles.Body.copy(color = AppColors.OnDarkMuted),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 22.dp),
                )

                CameraFocusOverlay()

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
                        isSelected = entry == zoom,
                        onClick = { onZoomChange(entry) },
                        selectedContainerColor = AppColors.Cream,
                        selectedContentColor = AppColors.Charcoal,
                        unselectedContainerColor = AppColors.OnDark.copy(alpha = 0.14f),
                        unselectedContentColor = AppColors.OnDarkMuted,
                    )
                }
            }

            CameraBottomBar(
                onShutter = onShutter,
                shutterContentDescription = stringResource(R.string.camera_shutter),
                modifier = Modifier.padding(start = 44.dp, end = 44.dp, top = 26.dp, bottom = 44.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenPreview() {
    PlantDexTheme {
        CameraScreen(zoom = CameraZoom.X1, onZoomChange = {}, onShutter = {}, onClose = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, heightDp = 780)
@Composable
private fun CameraScreenZoomedPreview() {
    PlantDexTheme {
        CameraScreen(zoom = CameraZoom.X2, onZoomChange = {}, onShutter = {}, onClose = {})
    }
}
