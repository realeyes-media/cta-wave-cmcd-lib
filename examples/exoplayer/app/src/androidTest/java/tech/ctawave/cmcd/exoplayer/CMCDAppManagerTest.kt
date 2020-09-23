package tech.ctawave.cmcd.exoplayer

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import tech.ctawave.exoplayercmcd.CMCDAppManager
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class CMCDAppManagerTest {
    @Test
    fun hello() {
        val cmcdAppManager = CMCDAppManager()
        assertEquals("hello!", cmcdAppManager.cmcdManager.hello)
    }
}
