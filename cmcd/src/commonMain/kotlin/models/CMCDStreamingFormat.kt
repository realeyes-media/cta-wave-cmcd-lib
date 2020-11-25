package models

/**
 * The streaming format which defines the current request.
 * If the streaming format being requested is unknown, then this key MUST NOT be used.
 */
enum class CMCDStreamingFormat(val token: String) {
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
