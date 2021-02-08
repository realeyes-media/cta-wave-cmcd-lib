package tech.ctawave.cmcd

import tech.ctawave.cmcd.models.CMCDObjectType
import tech.ctawave.cmcd.models.CMCDPayload
import tech.ctawave.cmcd.models.CMCDVersion
import tech.ctawave.cmcd.models.CMCDKey
import tech.ctawave.cmcd.models.CMCDStreamType
import tech.ctawave.cmcd.models.CMCDStreamingFormat
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@ExperimentalJsExport
@JsExport
object CMCDManagerCommonFactory {

    fun createCMCDManager(config: CMCDConfig): CMCDManager {
        return CMCDManagerCommon(config.contentId, config.sessionId, config.streamingFormat, config.version)
    }
}

@ExperimentalJsExport
@JsExport
class CMCDManagerCommon constructor(
    override var contentId: String,
    override var sessionId: String,
    override var streamingFormat: String,
    override var version: Int,
    ): CMCDManager {

    override var bufferLength: Int? = null
    override var encodedBitrate: Int? = null
    override var bufferStarvation = false
    override var objectDuration: Int? = null
    override var deadline: Int? = null
    override var measuredThroughput: Int? = null
    override var nextObjectRequest: String? = null
    override var nextRangeRequest: String? = null
    override var objectType: String? = null
    override var playbackRate: Double? = null
    override var requestedMaximumThroughput: Int? = null
    override var startup = false
    override var streamType: String? = null
    override var topBitrate: Int? = null

    private val id = CMCDPayload.ContentId(contentId)
    private val sid = CMCDPayload.SessionId(sessionId)
    private val sf = CMCDStreamingFormat.from(streamingFormat)?.let { CMCDPayload.StreamingFormat(it) }
    private val v = CMCDPayload.Version(CMCDVersion.from("$version") ?: CMCDVersion.VERSION_1)
    private val bl: CMCDPayload.BufferLength
        get() = CMCDPayload.BufferLength(bufferLength)
    private val br: CMCDPayload.EncodedBitrate
        get() = CMCDPayload.EncodedBitrate(encodedBitrate)
    private val bs: CMCDPayload.BufferStarvation
        get() = CMCDPayload.BufferStarvation(bufferStarvation)
    private val d: CMCDPayload.ObjectDuration
        get() = CMCDPayload.ObjectDuration(objectDuration)
    private val dl: CMCDPayload.Deadline
        get() = CMCDPayload.Deadline(deadline)
    private val mt: CMCDPayload.MeasuredThroughput
        get() = CMCDPayload.MeasuredThroughput(measuredThroughput)
    private val nor: CMCDPayload.NextObjectRequest
        get() = CMCDPayload.NextObjectRequest(nextObjectRequest)
    private val nrr: CMCDPayload.NextRangeRequest
        get() = CMCDPayload.NextRangeRequest(nextRangeRequest)
    private val ot: CMCDPayload.ObjectType?
        get() = CMCDObjectType.from(objectType)?.let { CMCDPayload.ObjectType(it) }
    private val pr: CMCDPayload.PlaybackRate
        get() = CMCDPayload.PlaybackRate(playbackRate)
    private val rmt: CMCDPayload.RequestedMaximumThroughput
        get() = CMCDPayload.RequestedMaximumThroughput(requestedMaximumThroughput)
    private val s: CMCDPayload.Startup
        get() = CMCDPayload.Startup(startup)
    private val st: CMCDPayload.StreamType?
        get() = CMCDStreamType.from(streamType)?.let { CMCDPayload.StreamType(it) }
    private val tb: CMCDPayload.TopBitrate
        get() = CMCDPayload.TopBitrate(topBitrate)

    override fun appendQueryParamsToUri(uri: String, objectType: String, startup: Boolean): String {
        this.objectType = objectType
        this.startup = startup

        val delimiter = if (uri.contains("?")) "&" else "?"
        val keyValues = buildKeyValues()

        val keyValuesStr = keyValues.map { entry ->
            val value = entry.value ?: ""
            val equal = if (value.isNotEmpty()) "%3D" else ""
            "${entry.key}$equal$value"
        }.joinToString("%2C")

        val queryParams = if (keyValuesStr.isNotEmpty()) "${delimiter}CMCD=$keyValuesStr" else ""

        // reset any one time use properties
        reset()

        return "$uri$queryParams"
    }

    override fun validate(queryParams: String): Boolean {
        var result = true

        // if queryParams contains CMCD=, strip it
        // lowercase for all string comparison
        val str = queryParams.replace("CMCD=", "").toLowerCase()

        val map = str.split("%2c").associate {
            val parts = it.split("%3d")
            if (parts.size > 1) {
                val (left, right) = parts
                left to right
            } else {
                parts[0] to "true"
            }
        }

        map.forEach { (key, value) ->
            run {
                if (!CMCDKey.matchingValueForKey(key, value)) {
                    result = false
                    return@forEach
                }
            }
        }

        return result
    }

    private fun buildKeyValues(): MutableMap<String, String?> {
        val result = mutableMapOf<String, String?>()

        // TODO: limit based on objectType
        bl.value?.let { result[bl.key.keyName] = convertToString(it) }
        br.value?.let { result[br.key.keyName] = convertToString(it) }
        if (bs.value) { result[bs.key.keyName] = null }
        if (CMCDKey.matchingValueForKey(id.key.keyName, id.value)) { result[id.key.keyName] = convertToString(id.value) }
        d.value?.let { result[d.key.keyName] = convertToString(it) }
        ot?.let { result[it.key.keyName] = it.value.token }
        dl.value?.let { result[dl.key.keyName] = convertToString(it) }
        mt.value?.let { result[mt.key.keyName] = convertToString(it) }
        nor.value?.let { result[nor.key.keyName] = convertToString(it) }
        nrr.value?.let { r ->
            if (CMCDKey.matchingValueForKey(nrr.key.keyName, r)) { result[nrr.key.keyName] = convertToString(r) }
        }
        pr.value?.let { r ->
            if (r != 1.0) { result[pr.key.keyName] = CMCDUtil.doubleToString(r, 1) }
        }
        rmt.value?.let { result[rmt.key.keyName] = convertToString(it) }
        result[sid.key.keyName] = convertToString(sid.value)
        if (s.value) { result[s.key.keyName] = null }
        sf?.let { result[it.key.keyName] = it.value.token }
        st?.let { s ->
            s.value?.let { result[s.key.keyName] = it.token }
        }
        tb.value?.let { result[tb.key.keyName] = convertToString(it) }
        if (v.value != CMCDVersion.VERSION_1) { result[v.key.keyName] = convertToString(v.value.number) }

        return result
    }

    private fun convertToString(v: Any?): String {
        return when (v) {
            is String -> CMCDUtil.encode("\"$v\"")
            else -> "$v"
        }
    }

    private fun reset() {
        bufferStarvation = false
        nextRangeRequest = null
        nextObjectRequest = null
    }

}
