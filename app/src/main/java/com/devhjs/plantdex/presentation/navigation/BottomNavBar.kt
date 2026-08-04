package com.devhjs.plantdex.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
 * 디자인에는 아이콘 대신 점만 있어서 그대로 점으로 그린다. 실제 아이콘이 생기면 [Dot] 만 교체하면 된다.
 *
 * 하단 시스템 인셋은 화면을 감싸는 Scaffold 가 처리한다.
 */
@Composable
fun BottomNavBar(
    selected: Route.Tab,
    onSelect: (Route.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(AppColors.Cream)) {
        HorizontalDivider(color = AppColors.Line)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.screenH, vertical = 12.dp),
        ) {
            NavItem.entries.forEach { item ->
                NavCell(
                    item = item,
                    isSelected = item.tab == selected,
                    onClick = { onSelect(item.tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun NavCell(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .semantics { selected = isSelected }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Dot(isSelected = isSelected)
        Text(
            text = stringResource(item.labelRes),
            style = if (isSelected) {
                AppTextStyles.NavLabel.copy(
                    color = AppColors.InkSoft,
                    fontWeight = FontWeight.SemiBold,
                )
            } else {
                AppTextStyles.NavLabel
            },
        )
    }
}

@Composable
private fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(7.dp)
            .background(
                color = if (isSelected) AppColors.InkSoft else AppColors.DotInactive,
                shape = CircleShape,
            ),
    )
}

private enum class NavItem(val tab: Route.Tab, @param:StringRes val labelRes: Int) {
    Home(Route.Home, R.string.nav_home),
    Collection(Route.Collection, R.string.nav_collection),
    Map(Route.Map, R.string.nav_map),
    Profile(Route.Profile, R.string.nav_profile),
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun BottomNavBarPreview() {
    PlantDexTheme {
        BottomNavBar(selected = Route.Home, onSelect = {})
    }
}
