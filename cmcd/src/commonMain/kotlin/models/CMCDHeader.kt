package models

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
