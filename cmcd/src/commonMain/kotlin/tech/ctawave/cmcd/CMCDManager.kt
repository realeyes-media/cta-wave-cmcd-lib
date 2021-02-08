package tech.ctawave.cmcd

import tech.ctawave.cmcd.models.CMCDVersion
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * CMCD Configuration
 *
 * @param contentId unique string identifying the current content. maximum length of 64 characters.
 * @param streamingFormat
 *  For Non JavaScript implementation:
 *
 *  streamingFormat: CMCDStreamingFormat.token
 *
 *  For JavaScript implementation:
 *
 *  streamingFormat: "h" (hls) |
 *      "d" (mpeg dash) |
 *      "s" (smooth streaming) |
 *      "o" (other)
 * @param sessionId a GUID representing current playback session. One is created by default.
 * @param version library version. Defaulted to 1.
 */
@ExperimentalJsExport
@JsExport
class CMCDConfig(val contentId: String, val streamingFormat: String, val sessionId: String = CMCDUtil.generateUUID(), val version: Int = CMCDVersion.VERSION_1.number)

/**
 * CMCD Manager
 */
@ExperimentalJsExport
@JsExport
interface CMCDManager {

    /**
     * buffer in milliseconds
     */
    var bufferLength: Int?

    /**
     * bitrate in kbps
     */
    var encodedBitrate: Int?

    /**
     * buffer has starved since last request
     */
    var bufferStarvation: Boolean

    /**
     * unique string identifying the current content. maximum length of 64 characters.
     */
    var contentId: String

    /**
     * playback duration in milliseconds of object being requested
     */
    var objectDuration: Int?

    /**
     * milliseconds from the request time until the first sample needs to be available in order to prevent a buffer under-run or other playback problems
     */
    var deadline: Int?

    /**
     * throughput between client and server in kbps
     */
    var measuredThroughput: Int?

    /**
     * relative path of the next object to be requested
     */
    var nextObjectRequest: String?

    /**
     * next request byte range. string of the form "<range-start>-", "<range-start>-<range-end>", or "-<suffix-length>"
     */
    var nextRangeRequest: String?

    /**
     * current media object being requested
     */
    var objectType: String?

    /**
     * current playback rate
     */
    var playbackRate: Double?

    /**
     * requested maximum throughput that the client considers sufficient for delivery of the asset
     */
    var requestedMaximumThroughput: Int?

    /**
     * a GUID representing current playback session
     */
    var sessionId: String

    /**
     * streaming format defining current request
     */
    var streamingFormat: String

    /**
     * indicates object is needed urgently due to startup, seeking, or recovery.
     */
    var startup: Boolean

    /**
     * stream type, LIVE or VOD
     */
    var streamType: String?

    /**
     * highest bitrate rendition in the manifest or playlist that client is allowed to play in kbps
     */
    var topBitrate: Int?

    /**
     * library version
     */
    var version: Int

    /**
     *
     * @param url string to append cmcd query params to
     * @param objectType
     *  For Non JavaScript implementations:
     *
     *  objectType: CMCDObjectType.token
     *
     *  For JavaScript Implementations:
     *
     *  objectType: "m" (manifest) |
     *      "a" (audio only) |
     *      "v" (video only) |
     *      "av" (muxed audio video) |
     *      "i" (init segment) |
     *      "c" (caption or subtitle) |
     *      "tt" (time) |
     *      "k" (cryptographic key license or certificate) |
     *      "o" (other)
     * @param startup object is urgently needed due to startup
     */
    fun appendQueryParamsToUri(uri: String, objectType: String, startup: Boolean = false): String

    /**
     * Validates supplied CMCD query string
     */
    fun validate(queryParams: String): Boolean
}

/**
 * Creates CMCDManager Class using supplied CMCDConfig
 */
@ExperimentalJsExport
expect object CMCDManagerFactory {
    fun createCMCDManager(config: CMCDConfig): CMCDManager
}
