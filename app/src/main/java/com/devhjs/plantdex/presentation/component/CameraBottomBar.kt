package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
 * 갤러리 · 셔터 · 전환 한 줄.
 *
 * 갤러리는 아직 동작이 없어서 clickable 을 붙이지 않는다. 콜백이 생기면 붙인다.
 */
@Composable
fun CameraBottomBar(
    onShutter: () -> Unit,
    onFlipLens: () -> Unit,
    shutterContentDescription: String,
    flipContentDescription: String,
    modifier: Modifier = Modifier,
    isShutterEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StripeSurface(
            modifier = Modifier
                .size(46.dp)
                .border(
                    width = 1.5.dp,
                    color = AppColors.OnDark.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(AppRadii.field),
                ),
            shape = RoundedCornerShape(AppRadii.field),
            baseColor = AppColors.CharcoalRaise,
            stripeColor = AppColors.CharcoalRaise2,
        )

        CameraShutterButton(
            onClick = onShutter,
            contentDescription = shutterContentDescription,
            enabled = isShutterEnabled,
        )

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(1.5.dp, AppColors.OnDark.copy(alpha = 0.4f), CircleShape)
                .clickable(role = Role.Button, onClick = onFlipLens)
                .semantics { contentDescription = flipContentDescription },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera_flip),
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = AppColors.OnDarkMuted,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390)
@Composable
private fun CameraBottomBarPreview() {
    PlantDexTheme {
        CameraBottomBar(
            onShutter = {},
            onFlipLens = {},
            shutterContentDescription = "촬영",
            flipContentDescription = "전후면 전환",
            modifier = Modifier.padding(horizontal = 44.dp, vertical = 26.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390)
@Composable
private fun CameraBottomBarCapturingPreview() {
    PlantDexTheme {
        CameraBottomBar(
            onShutter = {},
            onFlipLens = {},
            shutterContentDescription = "촬영",
            flipContentDescription = "전후면 전환",
            modifier = Modifier.padding(horizontal = 44.dp, vertical = 26.dp),
            isShutterEnabled = false,
        )
    }
}
