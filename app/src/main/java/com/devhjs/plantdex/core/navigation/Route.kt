package com.devhjs.plantdex.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * 백스택에 쌓이는 키. 프로세스 재시작 후 복원되므로 전부 @Serializable 이어야 한다.
 */
sealed interface Route : NavKey {

    /** 바텀 네비 탭. 탭마다 백스택을 따로 가진다. */
    sealed interface Tab : Route

    @Serializable
    data object Home : Tab

    @Serializable
    data object Collection : Tab

    @Serializable
    data object Record : Tab

    @Serializable
    data object Profile : Tab

    @Serializable
    data object Camera : Route

    /** 사진 자체는 키에 실을 수 없어 위치만 넘긴다. CameraX 가 붙기 전까지는 null 이다. */
    @Serializable
    data class Analyze(val photoUri: String?) : Route

    @Serializable
    data class Detail(val entryId: Long) : Route
}
