package tech.ctawave.exoApp.data.entities

enum class StreamingFormat {
    HLS,
    MPEG_DASH,
    SMOOTH_STREAMING;

    companion object {
        /**
         * Defaults to HLS for example purposes
         */
        fun fromString(str: String): StreamingFormat {
            return when (str) {
                "hls" -> HLS
                "dash" -> MPEG_DASH
                "smooth" -> SMOOTH_STREAMING
                else -> HLS
            }
        }
    }
}
