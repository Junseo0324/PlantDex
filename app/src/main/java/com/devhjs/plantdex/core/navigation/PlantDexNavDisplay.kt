package com.devhjs.plantdex.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.devhjs.plantdex.presentation.analyze.AnalyzeScreenRoot
import com.devhjs.plantdex.presentation.camera.CameraScreenRoot
import com.devhjs.plantdex.presentation.collection.CollectionScreenRoot
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.detail.DetailScreenRoot
import com.devhjs.plantdex.presentation.home.HomeScreenRoot
import com.devhjs.plantdex.presentation.profile.ProfileScreenRoot
import com.devhjs.plantdex.presentation.record.RecordScreenRoot

@Composable
fun PlantDexNavDisplay(modifier: Modifier = Modifier) {
    val stacks = mapOf(
        Route.Home to rememberNavBackStack(Route.Home),
        Route.Collection to rememberNavBackStack(Route.Collection),
        Route.Record to rememberNavBackStack(Route.Record),
        Route.Profile to rememberNavBackStack(Route.Profile),
    )

    var tabIndex by rememberSaveable { mutableIntStateOf(0) }
    val goToTab: (Route.Tab) -> Unit = { tabIndex = NavTabs.indexOf(it) }

    val entries = mapOf(
        Route.Home to
            rememberTabEntries(Route.Home, stacks.getValue(Route.Home), goToTab),
        Route.Collection to
            rememberTabEntries(Route.Collection, stacks.getValue(Route.Collection), goToTab),
        Route.Record to
            rememberTabEntries(Route.Record, stacks.getValue(Route.Record), goToTab),
        Route.Profile to
            rememberTabEntries(Route.Profile, stacks.getValue(Route.Profile), goToTab),
    )

    val currentTab = NavTabs[tabIndex]
    val currentStack = stacks.getValue(currentTab)

    // 탭 루트에서는 NavDisplay 가 백핸들러를 끈다 — 그 백스택에 이전 항목이 없기 때문.
    // 그대로 두면 앱이 종료되므로, 홈이 아닌 탭이면 홈으로 되돌린다.
    BackHandler(enabled = currentTab != Route.Home && currentStack.size == 1) {
        tabIndex = 0
    }

    // 인셋은 화면이 각자 처리한다. 카메라가 상태바 뒤까지 배경을 깔아야 하기 때문.
    NavDisplay(
        entries = entries.getValue(currentTab),
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Cream),
        onBack = { currentStack.popOrIgnore() },
    )
}

/**
 * 탭 하나의 백스택을 화면 목록으로 만든다.
 * 데코레이터도 탭마다 새로 만들어야 탭별 상태가 서로 섞이지 않는다.
 */
@Composable
private fun rememberTabEntries(
    root: Route.Tab,
    backStack: NavBackStack<NavKey>,
    onGoToTab: (Route.Tab) -> Unit,
): List<NavEntry<NavKey>> {
    // 탭 루트에서 뒤로 가면 스택이 비는데, NavDisplay 는 빈 목록을 받으면 예외를 던진다.
    if (backStack.isEmpty()) backStack.add(root)

    val decorators = listOf<NavEntryDecorator<NavKey>>(
        // SceneSetup 데코레이터는 NavDisplay 가 내부에서 붙인다.
        rememberSaveableStateHolderNavEntryDecorator(),
        // 이게 있어야 각 entry 안에서 hiltViewModel() 이 entry 수명에 묶인다.
        rememberViewModelStoreNavEntryDecorator(),
    )

    // 아래 람다들은 백스택이 바뀔 때까지 재생성되지 않는다.
    // 그래서 캡처하는 건 전부 안정된 참조여야 한다 — 여기서는 root 와 backStack.
    val onSelectTab: (Route.Tab) -> Unit = { tab ->
        // 보고 있는 탭을 다시 누르면 그 탭의 루트로 돌아간다.
        if (tab == root) backStack.popToRoot() else onGoToTab(tab)
    }

    return rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = decorators,
        entryProvider = entryProvider {
            entry<Route.Home> {
                TabScaffold(Route.Home, onSelectTab) {
                    HomeScreenRoot(
                        onDiscover = { backStack.add(Route.Camera) },
                        onSeeAllCollection = { onGoToTab(Route.Collection) },
                        onOpenDetail = { backStack.add(Route.Detail(entryId = it)) },
                    )
                }
            }
            entry<Route.Collection> {
                TabScaffold(Route.Collection, onSelectTab) {
                    CollectionScreenRoot(
                        onOpenDetail = { backStack.add(Route.Detail(entryId = it)) },
                    )
                }
            }
            entry<Route.Record> {
                TabScaffold(Route.Record, onSelectTab) { RecordScreenRoot() }
            }
            entry<Route.Profile> {
                TabScaffold(Route.Profile, onSelectTab) { ProfileScreenRoot() }
            }

            entry<Route.Camera>(clazzContentKey = { root.scope(it) }) {
                CameraScreenRoot(
                    onCaptured = { photoUri -> backStack.add(Route.Analyze(photoUri)) },
                    onClose = { backStack.popOrIgnore() },
                )
            }
            entry<Route.Analyze>(clazzContentKey = { root.scope(it) }) { key ->
                AnalyzeScreenRoot(
                    photoUri = key.photoUri,
                    onRegistered = { backStack.finishDiscovery(it) },
                    onRetake = { backStack.popOrIgnore() },
                )
            }
            entry<Route.Detail>(clazzContentKey = { root.scope(it) }) { key ->
                DetailScreenRoot(
                    entryId = key.entryId,
                    onBack = { backStack.popOrIgnore() },
                )
            }
        },
    )
}

/**
 * 탭마다 백스택이 따로라 같은 키가 두 탭에 동시에 있을 수 있다 — 홈과 도감에서 같은 상세를 연 경우.
 * contentKey 를 탭 단위로 갈라 두 화면이 ViewModel 을 공유하지 않게 한다.
 */
private fun Route.Tab.scope(key: Route): String = "${this::class.simpleName}/$key"

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

private fun MutableList<NavKey>.popToRoot() {
    while (size > 1) removeAt(lastIndex)
}

/** 발견 플로우(촬영 → 분석·연출)를 통째로 걷어내고 상세로 갈아끼운다. */
private fun MutableList<NavKey>.finishDiscovery(entryId: Long) {
    removeAll { it is Route.Camera || it is Route.Analyze }
    add(Route.Detail(entryId))
}
