package tech.ctawave.cmcd

import tech.ctawave.cmcd.models.CMCDObjectType
import tech.ctawave.cmcd.models.CMCDStreamType
import tech.ctawave.cmcd.models.CMCDStreamingFormat
import kotlin.js.ExperimentalJsExport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalJsExport
class CMCDManagerTests {
    @Test
    fun canCreateManager() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)
        assertNotNull(manager)

        assertEquals("testContentId", manager.contentId, "ContentId Mismatch")
        assertEquals("testSessionId", manager.sessionId, "SessionId Mismatch")
        assertEquals(CMCDStreamingFormat.HLS.token, manager.streamingFormat, "StreamingFormat Mismatch")
    }

    @Test
    fun createsValidUUID() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token)

        assertNotNull(config.sessionId)
        assertTrue(config.sessionId.isNotEmpty())
        assertFalse(config.sessionId.contains("undefined"))
    }

    @Test
    fun createsValidUrl() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)
        manager.bufferLength = 123456
        manager.playbackRate = 1.0

        val actual = manager.appendQueryParamsToUri("https://example.com/path/to/test", CMCDObjectType.MANIFEST.token)

        assertEquals("https://example.com/path/to/test?CMCD=bl%3D123456%2Ccid%3D%22testContentId%22%2Cot%3Dm%2Csid%3D%22testSessionId%22%2Csf%3Dh", actual)
    }

    @Test
    fun createsValidUrlThatHasQueryParamsAlready() {
        val config = CMCDConfig("testContentId",  CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)
        manager.bufferLength = 123456
        manager.playbackRate = 1.5

        val actual = manager.appendQueryParamsToUri("https://example.com/path/to/test?foo=bar", CMCDObjectType.INIT_SEGMENT.token)

        assertEquals("https://example.com/path/to/test?foo=bar&CMCD=bl%3D123456%2Ccid%3D%22testContentId%22%2Cot%3Di%2Cpr%3D1.5%2Csid%3D%22testSessionId%22%2Csf%3Dh", actual)
    }

    @Test
    fun createsValidUrlForFullParams() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.MPEG_DASH.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)
        manager.bufferLength = 123456
        manager.encodedBitrate = 64000
        manager.bufferStarvation = true
        manager.objectDuration = 2000
        manager.deadline = 4000
        manager.measuredThroughput = 10000
        manager.nextObjectRequest = "/next/object/request.mp4"
        manager.nextRangeRequest = "123-456"
        manager.playbackRate = 0.0
        manager.requestedMaximumThroughput = 50000
        manager.streamType = CMCDStreamType.LIVE.token
        manager.topBitrate = 40000

        val actual = manager.appendQueryParamsToUri("https://example.com/path/to/test", CMCDObjectType.VIDEO_ONLY.token, true)
        assertEquals("https://example.com/path/to/test?CMCD=bl%3D123456%2Cbr%3D64000%2Cbs%2Ccid%3D%22testContentId%22%2Cd%3D2000%2Cot%3Dv%2Cdl%3D4000%2Cmtp%3D10000%2Cnor%3D%22%2Fnext%2Fobject%2Frequest.mp4%22%2Cnrr%3D%22123-456%22%2Cpr%3D0.0%2Crtp%3D50000%2Csid%3D%22testSessionId%22%2Csu%2Csf%3Dd%2Cst%3Dl%2Ctb%3D40000", actual)
    }

    @Test
    fun correctlyValidatesValidQueryParams() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)

        val valid = manager.validate("CMCD=bl%3D123456%2Cbr%3D64000%2Cbs%2Ccid%3D%22testContentId%22%2Cd%3D2000%2Cot%3Dv%2Cdl%3D4000%2Cmtp%3D10000%2Cnor%3D%22%2Fnext%2Fobject%2Frequest.mp4%22%2Cnrr%3D%22123-456%22%2Cpr%3D0%2Crtp%3D50000%2Csid%3D%22testSessionId%22%2Csu%2Csf%3Dd%2Cst%3Dl%2Ctb%3D40000")
        assertTrue(valid)
    }

    @Test
    fun correctlyValidatesQueryParamsWithoutCMCDKey() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)

        val valid = manager.validate("bl%3D123456%2Cbr%3D64000%2Cbs%2Ccid%3D%22testContentId%22%2Cd%3D2000%2Cot%3Dv%2Cdl%3D4000%2Cmtp%3D10000%2Cnor%3D%22%2Fnext%2Fobject%2Frequest.mp4%22%2Cnrr%3D%22123-456%22%2Cpr%3D0%2Crtp%3D50000%2Csid%3D%22testSessionId%22%2Csu%2Csf%3Dd%2Cst%3Dl%2Ctb%3D40000")
        assertTrue(valid)
    }

    @Test
    fun correctlyValidatesInvalidQueryParams() {
        val config = CMCDConfig("testContentId", CMCDStreamingFormat.HLS.token, "testSessionId")
        val manager = CMCDManagerFactory.createCMCDManager(config)

        val valid = manager.validate("bl%3D4.5%2Cbr%3D64000%2Cbs%2Ccid%3D%22testContentId%22%2Cd%3D2000%2Cot%3Dv%2Cdl%3D4000%2Cmtp%3D10000%2Cnor%3D%22%2Fnext%2Fobject%2Frequest.mp4%22%2Cnrr%3D%22123-456%22%2Cpr%3D0%2Crtp%3D50000%2Csid%3D%22testSessionId%22%2Csu%2Csf%3Dd%2Cst%3Dl%2Ctb%3D40000")
        assertFalse(valid)
    }
}
