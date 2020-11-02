import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        manager.setBitrate(Version1Keys.bl.name, 10)
        manager.setStreamingFormat(Version1Keys.sf.name, CMCDStreamingFormat.d)
        val queryParams = "br=10,bl=10,sf=h"
        val actualValidation = manager.validate(queryParams)
        assertTrue(actualValidation)
    }
}
