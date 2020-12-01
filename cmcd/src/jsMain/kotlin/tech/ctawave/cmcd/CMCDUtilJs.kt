package tech.ctawave.cmcd

external fun encodeURIComponent(str: String): String

actual object CMCDUtil {
    actual fun encode(str: String): String {
        return encodeURIComponent(str)
    }

    actual fun doubleToString(d: Double, digits: Int): String {
        return js("d.toFixed(digits)") as String
    }

    actual fun generateUUID(): String {
        // taken from https://stackoverflow.com/a/2117523
        return js("([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, function (c) { return (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16) });") as String
    }
}
