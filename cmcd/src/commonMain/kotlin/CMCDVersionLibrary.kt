// NOTE (ben.toofer@realeyes.com): Look into using the delagate pattern
// https://proandroiddev.com/dont-reinvent-the-wheel-delegate-it-eac132f2aa64
// https://kotlinlang.org/docs/reference/delegated-properties.html
// This was a suggestion form the kotlin slack channel

enum class Version1Keys {
    bl,     // Buffer length
    br,     // Encoded bitrate
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

enum class CMCDHeaderName {
    Object,
    Request,
    Status,
    Session
}

enum class CMCDObjectType {
    m,      //  text file, such as a manifest or playlist
    a,      // audio only
    v,      // video only
    av,     // muxed audio and video
    i,      // init segment
    c,      // caption or subtitle
    tt,     //  ISOBMFF timed text track
    k,      // cryptographic key, license or certificate
    o       // other
}

enum class CMCDStreamingFormat {
    d,      // MPEG DASH
    h,      // HTTP Live Streaming (HLS)
    s,      // Smooth Streaming
    o       // Other
}

enum class CMCDStreamType {
    v,      // VOD
    l       // LIVE
}

data class CMCDProperty<T>(val header: CMCDHeaderName, val description: String, var value: T) {}

abstract class Version(var map: MutableMap<String, CMCDProperty<*>>)

class Version1(map: MutableMap<String, CMCDProperty<*>>) : Version(map) {

}


enum class VersionLibrary {
    VERSION_1,
}




