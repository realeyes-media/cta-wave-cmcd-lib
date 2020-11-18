data class CMCDProperty<T>(val header: CMCDHeader, var value: T?)

class Version(var map: MutableMap<CMCDPayloadKey, CMCDProperty<*>>)

enum class CMCDVersion(val value: Int) {
    VERSION_1(1);

    companion object {
        fun from(str: String?): CMCDVersion? {
            return when(str) {
                "1" -> VERSION_1
                else -> null
            }
        }
    }
}

interface CMCDKey {
    val key: String
    val header: CMCDHeader
}

enum class CommonKey(override val key: String, override val header: CMCDHeader): CMCDKey {
    /**
     * The media type of the current object being requested.
     *
     * See [CMCDObjectType].
     */
    OBJECT_TYPE("ot", CMCDHeader.OBJECT),

    /**
     * Key is included without a value if the object is needed urgently due to startup, seeking or recovery after a buffer-empty event.
     * The media SHOULD not be rendering when this request is made.
     * This key MUST not be sent if it is FALSE.
     */
    STARTUP("su", CMCDHeader.REQUEST),

    /**
     * The streaming format which defines the current request.
     *
     * If the streaming format being requested is unknown, the this key MUST NOT be used.
     *
     * See [CMCDStreamingFormat].
     */
    STREAMING_FORMAT("sf", CMCDHeader.SESSION),

    /**
     * The version of this specification used for interpreting the defined key names and values.
     * If this key is omitted, the client and server MUST interpret the values as being defined by version 1.
     * Client SHOULD omit this field if the version is 1.
     */
    VERSION("v", CMCDHeader.SESSION);

    companion object {
        fun from(str: String?): CommonKey? {
            return when (str) {
                "ot" -> OBJECT_TYPE
                "su" -> STARTUP
                "v" -> VERSION
                else -> null
            }
        }

        fun matchingValueForKey(key: String, value: String?): Boolean {
            return when(key) {
                "ot" -> CMCDObjectType.from(value) != null
                "su" -> isBoolean(value)
                "v" -> CMCDVersion.from(value) != null
                else -> false
            }
        }

        // TODO: duplicated
        // If the value type is BOOLEAN and the value is TRUE, then the equals sign and the value MUST be omitted.
        // This means that value of null returns true. Otherwise, true only if value == "false"
        private fun isBoolean(value: String?): Boolean {
            return value?.let {
                it == "false" || it == "true"
            } ?: false
        }
    }
}

/**
 * Payload keys associated with Version 1 of Common Media Client Data specification.
 */
enum class Version1Key(override val key: String, override val header: CMCDHeader): CMCDKey {
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
     * Current stream type.
     *
     * See [CMCDStreamType].
     */
    STREAM_TYPE("st", CMCDHeader.SESSION),

    /**
     * The highest bitrate rendition in the manifest or playlist that the client is allowed to play, given current codec, licensing and sizing constraints.
     */
    TOP_BITRATE("tb", CMCDHeader.OBJECT);

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

        // TODO: duplicated
        // If the value type is BOOLEAN and the value is TRUE, then the equals sign and the value MUST be omitted.
        // This means that value of null returns true. Otherwise, true only if value == "false"
        private fun isBoolean(value: String?): Boolean {
            return value?.let {
                it == "false" || it == "true"
            } ?: false
        }

        private fun isValidRangeRequest(value: String?): Boolean {
            return value?.matches(Regex("^%22((\\d+-\\d+)|(\\d+-)|(-\\d+))%22$")) ?: false
        }
    }
}

/**
 * The data payload for Header and Query Argument transmission consists of a series of key/value pairs constructed according to the following rules:
 *  1. All information in the payload MUST be represented as <key>=<value> pairs.
 *  2. The key and value MUST be separated by an equals sign Unicode 0x3D.
 *     If the value type is BOOLEAN and the value is TRUE, then the equals sign and the value MUST be omitted.
 *  3. Successive key/value pairs MUST be delimited by a comma Unicode 0x2C.
 *  4. The key names described in this specification are reserved.
 *     Custom key names may be used, but they MUST carry a hyphenated prefix to ensure that there will not be a namespace collision with future revisions to this specification.
 *     Clients SHOULD use a reverse-DNS syntax when defining their own prefix.
 *  5. If headers are used for data transmission, then custom keys SHOULD be allocated to one of the four defined header names based upon their expected level of variability:
 *      CMCD-Request : keys whose values vary with each request.
 *      CMCD-Object : keys whose values vary with the object being requested.
 *      CMCD-Status : keys whose values don’t vary with every request or object.
 *      CMCD-Session : keys whose values are expected to be invariant over the life of the session.
 *  6. All key names are case-sensitive.
 *  7. Any value of type String MUST be enclosed by opening and closing double quotes Unicode 0x22.
 *     Double quotes and backslashes MUST be escaped using a backslash "\" Unicode 0x5C character.
 *     Any value of type Token does not require quoting.
 *  8. All keys are OPTIONAL.
 *  9. Key-value pairs SHOULD be sequenced in alphabetical order of the key name, in order to reduce the fingerprinting surface exposed by the player.
 *  10. If the data payload is transmitted as a query argument, then the entire payload string MUST be [URLEncoded](https://url.spec.whatwg.org/#application/x-www-form-urlencoded).
 *      Data payloads transmitted via headers MUST NOT be URLEncoded.
 *  11. The data payload syntax is intended to be compliant with Structured Field Values for [HTTP](https://www.ietf.org/id/draft-ietf-httpbis-header-structure-18.txt).
 *  12. Transport Layer Security SHOULD be used to protect all transmission of CMCD data.
 */
sealed class CMCDPayloadKey {
    abstract val cmcdKey: CMCDKey

    /**
     * Common payload key
     */
    data class Common(override val cmcdKey: CommonKey): CMCDPayloadKey()

    /**
     * Version 1 payload key
     */
    data class Version1(override val cmcdKey: Version1Key): CMCDPayloadKey()

    companion object {
        fun matchingValueForKey(key: String, value: String?, version: CMCDVersion = CMCDVersion.VERSION_1): Boolean {
            // check if key is common, then move to different versions
            CommonKey.from(key)?.let {
                return CommonKey.matchingValueForKey(key, value)
            }

            return when (version) {
                CMCDVersion.VERSION_1 -> Version1Key.matchingValueForKey(key, value)
            }
        }
    }
}

/**
 * Four headers are defined to transmit the data.
 * The payload key/value pairs are sharded over these headers based upon their expected level of entropy,
 * in order to assist with [HPACK/QPACK](https://httpwg.org/specs/rfc7541.html) header compression.
 */
enum class CMCDHeader(val key: String) {
    /** keys whose values vary with the object being request */
    OBJECT("CMCD-Object"),

    /** keys whose values vary with each request */
    REQUEST("CMCD-Request"),

    /** keys whose values are to be invariant over the life of the session */
    SESSION("CMCD-Session"),

    /** keys whose values don't vary with every request or object */
    STATUS("CMCD-Status")
}

/**
 * The media type of the current object being requested.
 * If the object type being requested is unknown, then this key MUST NOT be used.
 */
enum class CMCDObjectType(val value: String) {
    /** Text file, such as a manifest or playlist */
    MANIFEST("m"),

    /** Audio only segment */
    AUDIO_ONLY("a"),

    /** Video only segment */
    VIDEO_ONLY("v"),

    /** Muxed audio and video segment */
    MUXED_AUDIO_VIDEO("av"),

    /** Init segment */
    INIT_SEGMENT("i"),

    /** Caption or subtitle */
    CAPTION_OR_SUBTITLE("c"),

    /** ISOBMFF timed text track */
    TIMED_TEXT_TRACK("tt"),

    /** Cryptographic key, license or certificate */
    KEY("k"),

    /** Other */
    OTHER("o");

    companion object {
        fun from(str: String?): CMCDObjectType? {
            return when (str) {
                "m" -> MANIFEST
                "a" -> AUDIO_ONLY
                "v" -> VIDEO_ONLY
                "av" -> MUXED_AUDIO_VIDEO
                "i" -> INIT_SEGMENT
                "c" -> CAPTION_OR_SUBTITLE
                "tt" -> TIMED_TEXT_TRACK
                "k" -> KEY
                "o" -> OTHER
                else -> null
            }
        }
    }
}

/**
 * The streaming format which defines the current request.
 * If the streaming format being requested is unknown, then this key MUST NOT be used.
 */
enum class CMCDStreamingFormat(val value: String) {
    /** MPEG DASH */
    MPEG_DASH("d"),

    /** HLS */
    HLS("h"),

    /** Smooth Streaming */
    SMOOTH_STREAMING("s"),

    /** Other */
    OTHER("o");

    companion object {
        fun from(str: String?): CMCDStreamingFormat? {
            return when(str) {
                "d" -> MPEG_DASH
                "h" -> HLS
                "s" -> SMOOTH_STREAMING
                "o" -> OTHER
                else -> null
            }
        }
    }
}

/**
 * The stream type used by the current session.
 */
enum class CMCDStreamType(val value: String) {
    /** All segments are available */
    VOD("v"),

    /** Segments become available over time */
    LIVE("l");

    companion object {
        fun from(str: String?): CMCDStreamType? {
            return when(str) {
                "v" -> VOD
                "l" -> LIVE
                else -> null
            }
        }
    }
}
