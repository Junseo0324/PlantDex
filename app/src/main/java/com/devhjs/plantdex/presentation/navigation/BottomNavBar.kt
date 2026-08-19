package com.devhjs.plantdex.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/**
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
                // 배경·구분선은 화면 끝까지 가고 탭만 제스처 바 위로 올린다.
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
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
            .heightIn(min = 44.dp)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            // 라벨이 바로 아래 붙으므로 아이콘은 장식이다.
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (isSelected) AppColors.InkSoft else AppColors.InkDisabled,
        )
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

private enum class NavItem(
    val tab: Route.Tab,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    Home(Route.Home, R.string.nav_home, R.drawable.ic_nav_home),
    Collection(Route.Collection, R.string.nav_collection, R.drawable.ic_nav_collection),
    Record(Route.Record, R.string.nav_record, R.drawable.ic_nav_record),
    Profile(Route.Profile, R.string.nav_profile, R.drawable.ic_nav_profile),
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF8F3)
@Composable
private fun BottomNavBarPreview() {
    PlantDexTheme {
        BottomNavBar(selected = Route.Home, onSelect = {})
    }
}
