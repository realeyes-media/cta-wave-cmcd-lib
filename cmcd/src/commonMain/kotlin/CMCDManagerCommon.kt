import models.CMCDConfig
import models.CMCDObjectType
import models.CMCDPayload
import models.CMCDVersion
import models.CMCDKey

object CMCDManagerCommonFactory {
    fun createCMCDManager(config: CMCDConfig): CMCDManager {
        return CMCDManagerCommon(CMCDPayload.ContentId(config.contentId), CMCDPayload.SessionId(config.sessionId), CMCDPayload.StreamingFormat(config.streamingFormat), CMCDPayload.Version(config.version), config.debug)
    }
}

class CMCDManagerCommon(
    override var contentId: CMCDPayload.ContentId,
    override var sessionId: CMCDPayload.SessionId,
    override var streamingFormat: CMCDPayload.StreamingFormat,
    override var version: CMCDPayload.Version,
    override val debug: Boolean
    ): CMCDManager {

    override var bufferLength = CMCDPayload.BufferLength(null)
    override var encodedBitrate = CMCDPayload.EncodedBitrate(null)
    override var bufferStarvation = CMCDPayload.BufferStarvation(false)
    override var objectDuration = CMCDPayload.ObjectDuration(null)
    override var deadline = CMCDPayload.Deadline(null)
    override var measuredThroughput = CMCDPayload.MeasuredThroughput(null)
    override var nextObjectRequest = CMCDPayload.NextObjectRequest(null)
    override var nextRangeRequest = CMCDPayload.NextRangeRequest(null)
    override var objectType = CMCDPayload.ObjectType(CMCDObjectType.OTHER)
    override var playbackRate = CMCDPayload.PlaybackRate(null)
    override var requestedMaximumThroughput = CMCDPayload.RequestedMaximumThroughput(null)
    override var startup = CMCDPayload.Startup(false)
    override var streamType = CMCDPayload.StreamType(null)
    override var topBitrate = CMCDPayload.TopBitrate(null)

    override fun appendQueryParamsToUrl(url: String, objectType: CMCDObjectType, startup: Boolean): String {
        this.objectType.value = objectType
        this.startup.value = startup

        val delimiter = if (url.contains("?")) "&" else "?"
        val keyValues = buildKeyValues()

        val keyValuesStr = keyValues.map { entry ->
            val value = entry.value ?: ""
            val equal = if (value.isNotEmpty()) "%3D" else ""
            "${entry.key}$equal$value"
        }.joinToString("%2C")

        val queryParams = if (keyValuesStr.isNotEmpty()) "${delimiter}CMCD=$keyValuesStr" else ""

        // reset any one time use properties
        reset()

        return "$url$queryParams"
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
        bufferLength.value?.let { result[bufferLength.key.keyName] = convertToString(it) }
        encodedBitrate.value?.let { result[encodedBitrate.key.keyName] = convertToString(it) }
        if (bufferStarvation.value) { result[bufferStarvation.key.keyName] = null }
        if (CMCDKey.matchingValueForKey(contentId.key.keyName, contentId.value)) { result[contentId.key.keyName] = convertToString(contentId.value) }
        objectDuration.value?.let { result[objectDuration.key.keyName] = convertToString(it) }
        result[objectType.key.keyName] = objectType.value.token
        deadline.value?.let { result[deadline.key.keyName] = convertToString(it) }
        measuredThroughput.value?.let { result[measuredThroughput.key.keyName] = convertToString(it) }
        nextObjectRequest.value?.let { result[nextObjectRequest.key.keyName] = convertToString(it) }
        nextRangeRequest.value?.let { nrr ->
            if (CMCDKey.matchingValueForKey(nextRangeRequest.key.keyName, nrr)) { result[nextRangeRequest.key.keyName] = convertToString(nrr) }
        }
        playbackRate.value?.let { pr ->
            if (pr != 1.0) { result[playbackRate.key.keyName] = convertToString(pr) }
        }
        requestedMaximumThroughput.value?.let { result[requestedMaximumThroughput.key.keyName] = convertToString(it) }
        result[sessionId.key.keyName] = convertToString(sessionId.value)
        if (startup.value) { result[startup.key.keyName] = null }
        result[streamingFormat.key.keyName] = streamingFormat.value.token
        streamType.value?.let { result[streamType.key.keyName] = it.token }
        topBitrate.value?.let { result[topBitrate.key.keyName] = convertToString(it) }
        if (version.value != CMCDVersion.VERSION_1) { result[version.key.keyName] = convertToString(version.value.number) }

        return result
    }

    private fun convertToString(v: Any?): String {
        return when (v) {
            is String -> CMCDUrlEncoder.encode("\"$v\"")
            else -> "$v"
        }
    }

    private fun reset() {
        bufferStarvation.value = false
    }

}
