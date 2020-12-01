package tech.ctawave.cmcd

expect object CMCDUtil {
    /**
     * URI Encode
     */
    fun encode(str: String): String
    /**
     * Double to String with desired number of digits after decimal point
     */
    fun doubleToString(d: Double, digits: Int): String

    /**
     * Generate UUID
     */
    fun generateUUID(): String
}
