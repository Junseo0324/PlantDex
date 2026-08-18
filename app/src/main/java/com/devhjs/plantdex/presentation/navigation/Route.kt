package com.devhjs.plantdex.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 백스택에 쌓이는 키. 프로세스 재시작 후 복원되므로 전부 @Serializable 이어야 한다.
 */
sealed interface Route : NavKey {

    /** 바텀 네비 탭. 서로 쌓이지 않고 백스택 루트를 교체한다. */
    sealed interface Tab : Route

    @Serializable
    data object Home : Tab

    @Serializable
    data object Collection : Tab

    @Serializable
    data object Map : Tab

    @Serializable
    data object Profile : Tab

    @Serializable
    data object Camera : Route

    @Serializable
    data object Analyze : Route

    @Serializable
    data class Detail(val entryId: Long) : Route
}
