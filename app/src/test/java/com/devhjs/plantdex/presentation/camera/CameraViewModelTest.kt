package com.devhjs.plantdex.presentation.camera

import androidx.compose.ui.geometry.Offset
import com.devhjs.plantdex.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CameraViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private companion object {
        const val PHOTO_URI = "file:///data/photos/1.jpg"
    }

    private fun subject() = CameraViewModel()

    private fun TestScope.collectEvents(viewModel: CameraViewModel): List<CameraEvent> {
        val events = mutableListOf<CameraEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(events)
        }
        return events
    }

    /** 셔터를 눌러 촬영 대기에 들어간 ViewModel. */
    private fun TestScope.capturing(): CameraViewModel =
        subject().apply {
            onAction(CameraAction.Shutter)
            advanceUntilIdle()
        }

    @Test
    fun `초기 상태는 1x 자동 후면이고 촬영 중이 아니다`() = runTest {
        assertEquals(CameraState(), subject().state.value)
    }

    @Test
    fun `줌을 고르면 상태에 반영된다`() = runTest {
        val viewModel = subject()

        viewModel.onAction(CameraAction.SelectZoom(CameraZoom.X5))

        assertEquals(CameraZoom.X5, viewModel.state.value.zoom)
    }

    @Test
    fun `플래시는 자동에서 켬 끔 순으로 돌아온다`() = runTest {
        val viewModel = subject()
        val seen = mutableListOf(viewModel.state.value.flash)

        repeat(3) {
            viewModel.onAction(CameraAction.ToggleFlash)
            seen += viewModel.state.value.flash
        }

        assertEquals(
            listOf(CameraFlash.AUTO, CameraFlash.ON, CameraFlash.OFF, CameraFlash.AUTO),
            seen,
        )
    }

    @Test
    fun `렌즈를 토글하면 전면과 후면을 오간다`() = runTest {
        val viewModel = subject()

        viewModel.onAction(CameraAction.ToggleLens)
        assertEquals(CameraLens.FRONT, viewModel.state.value.lens)

        viewModel.onAction(CameraAction.ToggleLens)
        assertEquals(CameraLens.BACK, viewModel.state.value.lens)
    }

    @Test
    fun `셔터는 촬영 중으로 바꾸고 촬영을 요청한다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Shutter)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isCapturing)
        assertEquals(listOf(CameraEvent.RequestCapture), events)
    }

    @Test
    fun `촬영 중에 셔터를 연타해도 한 번만 요청한다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        repeat(3) { viewModel.onAction(CameraAction.Shutter) }
        advanceUntilIdle()

        assertEquals(listOf(CameraEvent.RequestCapture), events)
    }

    @Test
    fun `촬영 결과가 오면 촬영 중이 풀리고 사진 위치가 실린다`() = runTest {
        val viewModel = capturing()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Captured(PHOTO_URI))
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isCapturing)
        assertEquals(listOf(CameraEvent.Captured(PHOTO_URI)), events)
    }

    @Test
    fun `촬영에 실패하면 실패 이벤트가 나온다`() = runTest {
        val viewModel = capturing()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Captured(null))
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isCapturing)
        assertEquals(listOf(CameraEvent.CaptureFailed), events)
    }

    @Test
    fun `촬영을 마치면 다시 찍을 수 있다`() = runTest {
        val viewModel = capturing()
        viewModel.onAction(CameraAction.Captured(PHOTO_URI))
        advanceUntilIdle()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Shutter)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isCapturing)
        assertEquals(listOf(CameraEvent.RequestCapture), events)
    }

    /** 요청한 적 없는 결과가 흘러들어오면 상태가 흔들리면 안 된다. */
    @Test
    fun `촬영을 요청하지 않았으면 결과를 버린다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Captured(PHOTO_URI))
        advanceUntilIdle()

        assertEquals(CameraState(), viewModel.state.value)
        assertTrue(events.isEmpty())
    }

    @Test
    fun `탭한 자리로 초점 요청이 나간다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.FocusAt(Offset(12f, 34f)))
        advanceUntilIdle()

        assertEquals(listOf(CameraEvent.RequestFocus(Offset(12f, 34f))), events)
    }

    @Test
    fun `닫기는 닫기 이벤트를 낸다`() = runTest {
        val viewModel = subject()
        val events = collectEvents(viewModel)

        viewModel.onAction(CameraAction.Close)
        advanceUntilIdle()

        assertEquals(listOf(CameraEvent.Close), events)
    }
}
