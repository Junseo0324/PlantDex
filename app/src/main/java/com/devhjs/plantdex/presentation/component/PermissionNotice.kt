package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/** 카메라처럼 어두운 화면 위에서 권한을 안내한다. */
@Composable
fun PermissionNotice(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.screenH),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = AppTextStyles.TitleM.copy(color = AppColors.OnDark),
            textAlign = TextAlign.Center,
        )
        Text(
            text = description,
            style = AppTextStyles.Body.copy(color = AppColors.OnDarkMuted),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )
        AppButton(
            text = actionLabel,
            onClick = onAction,
            modifier = Modifier.padding(top = AppSpacing.sectionGap),
            containerColor = AppColors.Cream,
            contentColor = AppColors.Charcoal,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390, heightDp = 500)
@Composable
private fun PermissionNoticePreview() {
    PlantDexTheme {
        PermissionNotice(
            title = "카메라 권한이 필요해요",
            description = "식물을 촬영해 도감에 등록하려면 카메라 접근을 허용해주세요.",
            actionLabel = "권한 허용",
            onAction = {},
        )
    }
}

/** 영구 거부 상태. 앱에서 다시 물을 수 없어 설정으로 보낸다. */
@Preview(showBackground = true, backgroundColor = 0xFF221E1A, widthDp = 390, heightDp = 500)
@Composable
private fun PermissionNoticeDeniedPreview() {
    PlantDexTheme {
        PermissionNotice(
            title = "카메라 권한이 꺼져 있어요",
            description = "설정 > 앱 > PlantDex 에서 카메라 권한을 켜주세요.",
            actionLabel = "설정 열기",
            onAction = {},
        )
    }
}
