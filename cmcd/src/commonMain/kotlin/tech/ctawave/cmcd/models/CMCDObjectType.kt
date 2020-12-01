package tech.ctawave.cmcd.models

/**
 * The media type of the current object being requested.
 * If the object type being requested is unknown, then this key MUST NOT be used.
 */
enum class CMCDObjectType(val token: String) {
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
