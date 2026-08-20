package com.devhjs.plantdex.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MockUserRepositoryImplTest {

    private val subject = MockUserRepositoryImpl()

    @Test
    fun `고정 사용자 한 명을 흘린다`() = runTest {
        val profile = subject.observeProfile().first()

        assertEquals("이수현", profile.name)
        assertTrue(profile.joinedAt.toEpochMilliseconds() > 0)
    }

    @Test
    fun `아직 아바타가 없어 null 이다`() = runTest {
        assertNull(subject.observeProfile().first().avatarUri)
    }

    @Test
    fun `한 번 흘리고 끝난다`() = runTest {
        assertEquals(1, subject.observeProfile().toList().size)
    }
}
