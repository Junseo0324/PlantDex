package com.devhjs.plantdex.presentation.profile

import com.devhjs.plantdex.domain.model.PlantLevel
import com.devhjs.plantdex.domain.model.ProfileSummary
import com.devhjs.plantdex.domain.model.UserProfile
import com.devhjs.plantdex.domain.usecase.GetProfileSummaryUseCase
import com.devhjs.plantdex.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val user = UserProfile(
        name = "홍길동",
        joinedAt = Instant.fromEpochMilliseconds(1_772_409_600_000),
    )

    private val summaries = MutableStateFlow(ProfileSummary())
    private val getProfileSummary = mockk<GetProfileSummaryUseCase> {
        every { this@mockk.invoke() } returns summaries
    }

    private fun subject() = ProfileViewModel(getProfileSummary)

    @Test
    fun `초기 상태는 기본값이다`() = runTest {
        assertEquals(ProfileState(), subject().state.value)
    }

    @Test
    fun `요약이 상태로 옮겨진다`() = runTest {
        summaries.value = ProfileSummary(
            user = user,
            level = 4,
            levelTitle = PlantLevel.OBSERVER,
            discoveredCount = 23,
            favoriteCount = 5,
            memoCount = 8,
            nextLevelTarget = 40,
        )

        val state = subject().state.first { it != ProfileState() }

        assertEquals(user, state.summary.user)
        assertEquals(4, state.summary.level)
        assertEquals(PlantLevel.OBSERVER, state.summary.levelTitle)
        assertEquals(23, state.summary.discoveredCount)
        assertEquals(5, state.summary.favoriteCount)
        assertEquals(8, state.summary.memoCount)
        assertEquals(40, state.summary.nextLevelTarget)
    }

    @Test
    fun `도감이 바뀌면 통계가 따라 바뀐다`() = runTest {
        val viewModel = subject()
        summaries.value = ProfileSummary(discoveredCount = 1)
        assertEquals(1, viewModel.state.first { it.summary.discoveredCount == 1 }.summary.discoveredCount)

        summaries.value = ProfileSummary(discoveredCount = 2)

        assertEquals(2, viewModel.state.first { it.summary.discoveredCount == 2 }.summary.discoveredCount)
    }

    @Test
    fun `요약은 한 번만 구독한다`() = runTest {
        subject()
        advanceUntilIdle()

        verify(exactly = 1) { getProfileSummary() }
    }
}
