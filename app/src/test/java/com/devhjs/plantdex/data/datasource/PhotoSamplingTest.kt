package com.devhjs.plantdex.data.datasource

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PhotoSamplingTest {

    private companion object {
        const val MAX_EDGE = 1568
    }

    @Test
    fun `상한보다 작으면 줄이지 않는다`() {
        assertEquals(1, sampleSizeFor(longEdge = 1000, maxEdge = MAX_EDGE))
    }

    @Test
    fun `상한과 같으면 줄이지 않는다`() {
        assertEquals(1, sampleSizeFor(longEdge = MAX_EDGE, maxEdge = MAX_EDGE))
    }

    @Test
    fun `12메가픽셀 사진은 절반으로 줄인다`() {
        assertEquals(2, sampleSizeFor(longEdge = 4032, maxEdge = MAX_EDGE))
    }

    @Test
    fun `아주 큰 사진일수록 더 많이 줄인다`() {
        assertEquals(4, sampleSizeFor(longEdge = 8000, maxEdge = MAX_EDGE))
    }

    /** sample 을 한 단계만 더 줄여도 상한 밑으로 떨어져 리스케일에서 화질이 손해다. */
    @Test
    fun `줄인 뒤 장변은 상한 밑으로 내려가지 않는다`() {
        listOf(1569, 2000, 3136, 4032, 6000, 8000, 12000).forEach { longEdge ->
            val scaled = longEdge / sampleSizeFor(longEdge, MAX_EDGE)
            assertTrue("$longEdge -> $scaled", scaled >= MAX_EDGE)
        }
    }

    @Test
    fun `크기를 읽지 못한 경우에도 값이 나온다`() {
        assertEquals(1, sampleSizeFor(longEdge = 0, maxEdge = MAX_EDGE))
    }
}
