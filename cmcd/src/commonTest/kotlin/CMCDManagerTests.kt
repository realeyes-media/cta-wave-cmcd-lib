import kotlin.test.Test
import kotlin.test.assertEquals

class CMCDManagerTests {
    @Test
    fun helloCMCDManager() {
        val manager = CMCDManagerFactory.createCMCDManager()
        assertEquals("hello!", manager.hello)
    }
}
