package tech.ctawave.exoplayercmcd

import org.junit.Test
import org.junit.Assert.assertEquals

class CMCDAppManagerTest {
    @Test
    fun hello() {
        val cmcdAppManager = CMCDAppManager()
        assertEquals("hello!", cmcdAppManager.cmcdManager.hello)
    }
}
