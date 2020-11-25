package models

/**
 * Payload keys associated with Version 1 of Common Media Client Data specification.
 */
enum class CMCDKey(val keyName: String, val header: CMCDHeader) {
    /**
     * The buffer length associated with the media object being requested.
     * This value MUST be rounded to the nearest 100 ms.
     * This key SHOULD only be sent with an object type of ‘a’,‘v’ or ‘av’.
     */
    BUFFER_LENGTH("bl", CMCDHeader.OBJECT),

    /**
     * The encoded bitrate of the audio or video object being requested.
     * This may not be known precisely by the player, however it MAY be estimated based upon playlist/manifest declarations.
     * If the playlist declares both peak and average bitrate values, the peak value should be transmitted.
     */
    ENCODED_BITRATE("br", CMCDHeader.REQUEST),

    /**
     * Key is included without a value if the buffer was starved at some point between the prior request and this object request,
     * resulting in the player being in a re-buffering state and the video or audio playback being stalled.
     * This key MUST NOT be sent if the buffer was not starved since the prior request.
     * If the object type ‘ot’ key is sent along with this key, then the ‘bs’ key refers to the buffer associated with the particular object type.
     * If no object type is communicated, then the buffer state applies to the current session.
     */
    BUFFER_STARVATION("bs", CMCDHeader.STATUS),

    /**
     * A unique string identifying the current content. Maximum length is 64 characters.
     * This value is consistent across multiple different sessions and devices and is defined and updated at the discretion of the service provider.
     */
    CONTENT_ID("cid", CMCDHeader.SESSION),

    /**
     * The playback duration in milliseconds of the object being requested.
     * If a partial segment is being requested, then this value MUST indicate the playback duration of that part and not that of its parent segment.
     * This value can be an approximation of the estimated duration if the explicit value is not known.
     */
    OBJECT_DURATION("d", CMCDHeader.OBJECT),

    /**
     * The media type of the current object being requested.
     *
     * See [CMCDObjectType].
     */
    OBJECT_TYPE("ot", CMCDHeader.OBJECT),

    /**
     * Deadline from the request time until the first sample of this Segment/Object needs to be available
     * in order to not create a buffer under-run or any other playback problems.
     * This value MUST be rounded to the nearest 100ms.
     * For a playback rate of 1, this may be equivalent to the player’s remaining buffer length.
     */
    DEADLINE("dl", CMCDHeader.REQUEST),

    /**
     * The throughput between client and server, as measured by the client and MUST be rounded to the nearest 100 kbps.
     * This value, however derived, SHOULD be the value that the client is using to make its next Adaptive Bitrate switching decision.
     * If the client is connected to multiple servers concurrently, it must take care to report only the throughput measured against the receiving server.
     * If the client has multiple concurrent connections to the server,
     * then the intent is that this value communicates the aggregate throughput the client sees across all those connections.
     */
    MEASURED_THROUGHPUT("mtp", CMCDHeader.REQUEST),

    /**
     * Relative path of the next object to be requested.
     * This can be used to trigger pre-fetching by the CDN.
     * This MUST be a path relative to the current request.
     * This string MUST be [URLEncoded](https://url.spec.whatwg.org/#application/x-www-form-urlencoded).
     * The client SHOULD NOT depend upon any pre-fetch action being taken - it is merely a request for such a pre-fetch to take place.
     */
    NEXT_OBJECT_REQUEST("nor", CMCDHeader.REQUEST),

    /**
     * If the next request will be a partial object request, then this string denotes the byte range to be requested.
     * If the ‘nor’ field is not set, then the object is assumed to match the object currently being requested.
     * The client SHOULD NOT depend upon any pre-fetch action being taken - it is merely a request for such a pre-fetch to take place.
     * Formatting is similar to the HTTP Range header, except that the unit MUST be ‘byte’,
     * the ‘Range:’ prefix is NOT required and specifying multiple ranges is NOT allowed.
     *
     * Valid combinations are:
     *
     * "<range-start>-"
     * "<range-start>-<range-end>"
     * "-<suffix-length>"
     */
    NEXT_RANGE_REQUEST("nrr", CMCDHeader.REQUEST),

    /**
     * Current playback rate.
     *
     * Ex:
     * 0 = not playing
     * 0.5 = half speed
     * 1 = real-time
     * 2 = double speed
     */
    PLAYBACK_RATE("pr", CMCDHeader.SESSION),

    /**
     * The requested maximum throughput that the client considers sufficient for delivery of the asset.
     * Values MUST be rounded to the nearest 100kbps.
     * For example, a client would indicate that the current segment, encoded at 2Mbps, is to be delivered at no more than 10Mbps, by using rtp=10000.
     *
     * Note:
     * This can benefit clients by preventing buffer saturation through over-delivery and can also deliver a community benefit through fair-share delivery.
     * The concept is that each client receives the throughput necessary for great performance, but no more.
     * The CDN may not support the rtp feature.
     */
    REQUESTED_MAXIMUM_THROUGHPUT("rtp", CMCDHeader.SESSION),

    /**
     * A GUID identifying the current playback session.
     * A playback session typically ties together segments belonging to a single media asset.
     * Maximum length is 64 characters.
     * It is RECOMMENDED to conform to the [UUID specification](https://www.ietf.org/rfc/rfc4122.txt).
     */
    SESSION_ID("sid", CMCDHeader.SESSION),

    /**
     * The streaming format which defines the current request.
     *
     * If the streaming format being requested is unknown, the this key MUST NOT be used.
     *
     * See [CMCDStreamingFormat].
     */
    STREAMING_FORMAT("sf", CMCDHeader.SESSION),

    /**
     * Current stream type.
     *
     * See [CMCDStreamType].
     */
    STREAM_TYPE("st", CMCDHeader.SESSION),

    /**
     * Key is included without a value if the object is needed urgently due to startup, seeking or recovery after a buffer-empty event.
     * The media SHOULD not be rendering when this request is made.
     * This key MUST not be sent if it is FALSE.
     */
    STARTUP("su", CMCDHeader.REQUEST),

    /**
     * The highest bitrate rendition in the manifest or playlist that the client is allowed to play, given current codec, licensing and sizing constraints.
     */
    TOP_BITRATE("tb", CMCDHeader.OBJECT),

    /**
     * The version of this specification used for interpreting the defined key names and values.
     * If this key is omitted, the client and server MUST interpret the values as being defined by version 1.
     * Client SHOULD omit this field if the version is 1.
     */
    VERSION("v", CMCDHeader.SESSION);

    companion object {
        fun matchingValueForKey(key: String, value: String?): Boolean {
            return when(key) {
                "bl" -> isInt(value)
                "br" -> isInt(value)
                "bs" -> isBoolean(value)
                "cid" -> value != null
                "d" -> isInt(value)
                "dl" -> isInt(value)
                "mtp" -> isInt(value)
                "nor" -> value != null
                "nrr" -> isValidRangeRequest(value)
                "ot" -> CMCDObjectType.from(value) != null
                "pr" -> isDecimal(value)
                "rtp" -> isInt(value)
                "sf" -> CMCDStreamingFormat.from(value) != null
                "sid" -> value != null
                "st" -> CMCDStreamType.from(value) != null
                "su" -> isBoolean(value)
                "tb" -> isInt(value)
                "v" -> CMCDVersion.from(value) != null
                else -> false
            }
        }

        private fun isInt(value: String?): Boolean {
            return value?.let {
                it.toIntOrNull() != null
            } ?: false
        }

        private fun isDecimal(value: String?): Boolean {
            return value?.let {
                it.toFloatOrNull() != null
            } ?: false
        }

        // If the value type is BOOLEAN and the value is TRUE, then the equals sign and the value MUST be omitted.
        // This means that value of null returns true. Otherwise, true only if value == "false"
        private fun isBoolean(value: String?): Boolean {
            return value?.let {
                it == "false" || it == "true"
            } ?: false
        }

        private fun isValidRangeRequest(value: String?): Boolean {
            return value?.replace("%22", "")?.matches(Regex("^((\\d+-\\d+)|(\\d+-)|(-\\d+))$")) ?: false
        }
    }
}
