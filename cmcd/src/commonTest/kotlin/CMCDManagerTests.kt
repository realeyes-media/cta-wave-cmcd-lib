import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CMCDManagerTests {
    @Test
    fun canCreateManager() {
        val manager = CMCDManagerFactory.createCMCDManager("testContentId","testSessionId", CMCDStreamingFormat.HLS)
        assertNotNull(manager)
    }

    @Test
    fun createsValidUrl() {
        val manager = CMCDManagerFactory.createCMCDManager("testContentId","testSessionId", CMCDStreamingFormat.HLS)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.BUFFER_LENGTH), 123456)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.PLAYBACK_RATE), 1.0)

        val actual = manager.appendQueryParamsToUrl("https://example.com/path/to/test", CMCDObjectType.MANIFEST)

        assertEquals("https://example.com/path/to/test?CMCD=bl%3D123456%2Ccid%3D%22testContentId%22%2Cot%3Dm%2Cpr%3D1%2Csf%3Dh%2Csid%3D%22testSessionId%22", actual)
    }

    @Test
    fun createsValidUrlThatHasQueryParamsAlready() {
        val manager = CMCDManagerFactory.createCMCDManager("testContentId","testSessionId", CMCDStreamingFormat.HLS)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.BUFFER_LENGTH), 123456)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.PLAYBACK_RATE), 1.5)

        val actual = manager.appendQueryParamsToUrl("https://example.com/path/to/test?foo=bar", CMCDObjectType.INIT_SEGMENT)

        assertEquals("https://example.com/path/to/test?foo=bar&CMCD=bl%3D123456%2Ccid%3D%22testContentId%22%2Cot%3Di%2Cpr%3D1.5%2Csf%3Dh%2Csid%3D%22testSessionId%22", actual)
    }

    @Test
    fun createsValidUrlForFullParams() {
        val manager = CMCDManagerFactory.createCMCDManager("testContentId","testSessionId", CMCDStreamingFormat.MPEG_DASH)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.BUFFER_LENGTH), 123456)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.ENCODED_BITRATE), 64000)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.BUFFER_STARVATION), true)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.OBJECT_DURATION), 2000)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.DEADLINE), 4000)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.MEASURED_THROUGHPUT), 10000)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.NEXT_OBJECT_REQUEST), "\"/next/object/request.mp4\"")
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.NEXT_RANGE_REQUEST), "\"123-456\"")
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.PLAYBACK_RATE), 0.0)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.REQUESTED_MAXIMUM_THROUGHPUT), 50000)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.STREAM_TYPE), CMCDStreamType.LIVE.value)
        manager.setProperty(CMCDPayloadKey.Version1(Version1Key.TOP_BITRATE), 40000)

        val actual = manager.appendQueryParamsToUrl("https://example.com/path/to/test", CMCDObjectType.VIDEO_ONLY, true)
        assertEquals("https://example.com/path/to/test?CMCD=bl%3D123456%2Cbr%3D64000%2Cbs%2Ccid%3D%22testContentId%22%2Cd%3D2000%2Cdl%3D4000%2Cmtp%3D10000%2Cnor%3D%22%2Fnext%2Fobject%2Frequest.mp4%22%2Cnrr%3D%22123-456%22%2Cot%3Dv%2Cpr%3D0%2Crtp%3D50000%2Csf%3Dd%2Csid%3D%22testSessionId%22%2Cst%3Dl%2Csu%2Ctb%3D40000", actual)
    }

    @Test
    fun correctlyValidatesValidQueryParams() {}

    @Test
    fun correctlyValidatesInvalidQueryParams() {}
}
