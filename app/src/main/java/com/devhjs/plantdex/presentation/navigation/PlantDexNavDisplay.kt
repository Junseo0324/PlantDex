package com.devhjs.plantdex.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.devhjs.plantdex.presentation.analyze.AnalyzeScreenRoot
import com.devhjs.plantdex.presentation.camera.CameraScreenRoot
import com.devhjs.plantdex.presentation.collection.CollectionScreenRoot
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.detail.DetailScreenRoot
import com.devhjs.plantdex.presentation.home.HomeScreenRoot
import com.devhjs.plantdex.presentation.record.RecordScreenRoot

@Composable
fun PlantDexNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Route.Home)

    // 인셋은 화면이 각자 처리한다. 카메라가 상태바 뒤까지 배경을 깔아야 하기 때문.
    NavDisplay(
        backStack = backStack,
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream),
        onBack = { backStack.popOrIgnore() },
        // SceneSetup 데코레이터는 NavDisplay 가 내부에서 붙인다.
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // 이게 있어야 각 entry 안에서 hiltViewModel() 이 entry 수명에 묶인다.
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Home> {
                TabScaffold(Route.Home, backStack::switchTab) {
                    HomeScreenRoot(
                        onDiscover = { backStack.add(Route.Camera) },
                        onSeeAllCollection = { backStack.switchTab(Route.Collection) },
                        onOpenDetail = { backStack.add(Route.Detail(entryId = it)) },
                    )
                }
            }
            entry<Route.Collection> {
                TabScaffold(Route.Collection, backStack::switchTab) {
                    CollectionScreenRoot(
                        onOpenDetail = { backStack.add(Route.Detail(entryId = it)) },
                    )
                }
            }
            entry<Route.Record> {
                TabScaffold(Route.Record, backStack::switchTab) { RecordScreenRoot() }
            }
            entry<Route.Profile> {
                TabScaffold(Route.Profile, backStack::switchTab) {
                    PlaceholderScreen(title = "내정보")
                }
            }

            entry<Route.Camera> {
                CameraScreenRoot(
                    onShutter = { backStack.add(Route.Analyze) },
                    onClose = { backStack.popOrIgnore() },
                )
            }
            entry<Route.Analyze> {
                AnalyzeScreenRoot(
                    onRegistered = { backStack.finishDiscovery(it) },
                    onRetake = { backStack.popOrIgnore() },
                )
            }
            entry<Route.Detail> { key ->
                DetailScreenRoot(
                    entryId = key.entryId,
                    onBack = { backStack.popOrIgnore() },
                )
            }
        },
    )
}

/** 탭 화면 공통 뼈대. 본문 아래에 바텀 네비를 깐다. */
@Composable
private fun TabScaffold(
    selected: Route.Tab,
    onSelectTab: (Route.Tab) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 하단 인셋은 BottomNavBar 가 자기 배경을 깔면서 처리한다.
        Box(
            modifier = Modifier
                .weight(1f)
                .statusBarsPadding(),
        ) { content() }
        BottomNavBar(selected = selected, onSelect = onSelectTab)
    }
}

/** 루트까지 비우면 NavDisplay 가 그릴 게 없어지므로 마지막 하나는 남긴다. */
private fun MutableList<NavKey>.popOrIgnore() {
    if (size > 1) removeAt(lastIndex)
}

private fun MutableList<NavKey>.switchTab(tab: Route.Tab) {
    clear()
    add(tab)
}

/** 발견 플로우(촬영 → 분석·연출)를 통째로 걷어내고 상세로 갈아끼운다. */
private fun MutableList<NavKey>.finishDiscovery(entryId: Long) {
    removeAll { it is Route.Camera || it is Route.Analyze }
    add(Route.Detail(entryId))
}
