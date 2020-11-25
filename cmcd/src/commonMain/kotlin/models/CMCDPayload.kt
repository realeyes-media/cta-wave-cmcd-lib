package models

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
sealed class CMCDPayload {
    data class BufferLength(var value: Int?, val key: CMCDKey = CMCDKey.BUFFER_LENGTH)
    data class EncodedBitrate(var value: Int?, val key: CMCDKey = CMCDKey.ENCODED_BITRATE)
    data class BufferStarvation(var value: Boolean = false, val key: CMCDKey = CMCDKey.BUFFER_STARVATION)
    data class ContentId(val value: String, val key: CMCDKey = CMCDKey.CONTENT_ID)
    data class ObjectDuration(var value: Int?, val key: CMCDKey = CMCDKey.OBJECT_DURATION)
    data class ObjectType(var value: CMCDObjectType, val key: CMCDKey = CMCDKey.OBJECT_TYPE)
    data class Deadline(var value: Int?, val key: CMCDKey = CMCDKey.DEADLINE)
    data class MeasuredThroughput(var value: Int?, val key: CMCDKey = CMCDKey.MEASURED_THROUGHPUT)
    data class NextObjectRequest(var value: String?, val key: CMCDKey = CMCDKey.NEXT_OBJECT_REQUEST)
    data class NextRangeRequest(var value: String?, val key: CMCDKey = CMCDKey.NEXT_RANGE_REQUEST)
    data class PlaybackRate(var value: Double?, val key: CMCDKey = CMCDKey.PLAYBACK_RATE)
    data class RequestedMaximumThroughput(var value: Int?, val key: CMCDKey = CMCDKey.REQUESTED_MAXIMUM_THROUGHPUT)
    data class SessionId(var value: String, val key: CMCDKey = CMCDKey.SESSION_ID)
    data class Startup(var value: Boolean = false, val key: CMCDKey = CMCDKey.STARTUP)
    data class StreamingFormat(var value: CMCDStreamingFormat, val key: CMCDKey = CMCDKey.STREAMING_FORMAT)
    data class StreamType(var value: CMCDStreamType?, val key: CMCDKey = CMCDKey.STREAM_TYPE)
    data class TopBitrate(var value: Int?, val key: CMCDKey = CMCDKey.TOP_BITRATE)
    data class Version(var value: CMCDVersion = CMCDVersion.VERSION_1, val key: CMCDKey = CMCDKey.VERSION)
}
