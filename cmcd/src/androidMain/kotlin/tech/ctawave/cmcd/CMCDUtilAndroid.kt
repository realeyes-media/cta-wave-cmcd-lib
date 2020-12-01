package tech.ctawave.cmcd

import java.net.URLEncoder
import java.util.*

actual object CMCDUtil {
    actual fun encode(str: String): String {
        return URLEncoder.encode(str, "utf-8")
    }

    actual fun doubleToString(d: Double, digits: Int): String {
        return d.format(digits)
    }

    actual fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}
