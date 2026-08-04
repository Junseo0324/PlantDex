package com.devhjs.plantdex.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DexNumberTest {

    @Test
    fun `한 자리 번호는 세 자리로 채워진다`() {
        assertEquals("No.001", 1.toDexLabel())
        assertEquals("No.009", 9.toDexLabel())
    }

    @Test
    fun `두 자리 번호는 앞에 0 하나가 붙는다`() {
        assertEquals("No.023", 23.toDexLabel())
    }

    @Test
    fun `세 자리 번호는 그대로 쓰인다`() {
        assertEquals("No.100", 100.toDexLabel())
        assertEquals("No.999", 999.toDexLabel())
    }

    @Test
    fun `세 자리를 넘으면 잘리지 않고 늘어난다`() {
        assertEquals("No.1000", 1000.toDexLabel())
    }
}
