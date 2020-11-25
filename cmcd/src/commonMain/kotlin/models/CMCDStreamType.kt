package models

/**
 * The stream type used by the current session.
 */
enum class CMCDStreamType(val token: String) {
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
