package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/** 아직 눌러서 갈 곳이 없어 clickable 을 붙이지 않는다. 화살표는 표시만 한다. */
@Composable
fun SettingRow(
    label: String,
    modifier: Modifier = Modifier,
    value: String? = null,
    showDivider: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 44.dp)
                .padding(horizontal = 18.dp, vertical = 11.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, style = AppTextStyles.BodyStrong.copy(fontSize = 14.sp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                value?.let {
                    Text(
                        text = it,
                        style = AppTextStyles.Label.copy(
                            fontSize = 13.sp,
                            color = AppColors.InkPlaceholder,
                        ),
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AppColors.InkDisabled,
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = AppColors.LineStrong,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3, widthDp = 390)
@Composable
private fun SettingRowPreview() {
    PlantDexTheme {
        SpecList(modifier = Modifier.padding(AppSpacing.screenH)) {
            SettingRow(label = "알림 설정", value = "켜짐")
            SettingRow(label = "테마", value = "라이트")
            SettingRow(label = "데이터 백업")
            SettingRow(label = "도움말")
            SettingRow(label = "앱 정보", value = "1.0.0", showDivider = false)
        }
    }
}
