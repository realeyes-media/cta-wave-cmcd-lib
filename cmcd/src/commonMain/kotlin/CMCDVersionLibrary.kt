// NOTE (ben.toofer@realeyes.com): Look into using the delagate pattern
// https://proandroiddev.com/dont-reinvent-the-wheel-delegate-it-eac132f2aa64
// https://kotlinlang.org/docs/reference/delegated-properties.html
// This was a suggestion form the kotlin slack channel



interface IVersion2 {
    val bl: Int
    val br: String

}

enum class Version1Keys {
    bl,     // Encoded bitrate
    br,     // Buffer length
    bs,     // Buffer starvation
    cid,    // Content ID
    d,      // Object duration
    dl,     // Deadline
    mtp,    // Measured throughput
    nor,    // Next object request
    nrr,    // Next range request
    ot,     // Object type
    pr,     // Playback rate
    rtp,    // Requested maximum throughput
    sf,     // Streaming format
    sid,    // Session ID
    st,     // Stream type
    su,     // Startup
    tb,     // Top bitrate
    v       // Version
}

sealed class VersionLibrary {
    data class Version1 (val mappings: Map<Version1Keys, Any>): VersionLibrary()
    data class Version2 (val temp: IVersion2): IVersion2
}

expect object CMCDLibraryFactory {
    fun createCMCDManager(): CMCDManager
}

data class Version1 {
    val bl: Int
}

fun whatever() {
    val bl: Pair<String, Int>
    val temp = mutableMapOf<Version1Enum, Any>(
        Version1Enum.br to 5,
        Version1Enum.bl to ""
    )
    VersionLibrary.Version1(temp).temp.get()

    temp.get()
}

