import kotlin.test.Test
import kotlin.test.assertEquals

class CMCDManagerTests {
    @Test
    fun helloCMCDManager() {
        val manager = CMCDManagerFactory.createCMCDManager()
        assertEquals("hello!", manager.hello)
    }

    @Test
    fun createVersionLibrary() {
        val mockFactory = CMCDManagerFactoryFake()
        val manager = mockFactory.createCMCDManager(VersionLibrary.VERSION_1)
        manager.setBitrate(Version1Keys.br.name, 512)
        val actual = manager.queryParamMap["br"]
        val expected = 512
        assertEquals(expected, actual)
    }
}
