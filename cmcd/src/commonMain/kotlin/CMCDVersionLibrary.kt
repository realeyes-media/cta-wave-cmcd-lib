import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// NOTE (ben.toofer@realeyes.com): Look into using the delagate pattern
// https://proandroiddev.com/dont-reinvent-the-wheel-delegate-it-eac132f2aa64
// https://kotlinlang.org/docs/reference/delegated-properties.html
// This was a suggestion form the kotlin slack channel

sealed class VersionKeys {
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

data class CMCDProperty<T>( val header: CMCDHeaderName, val description: String, var value: T) {}

data class Version1(
    var br: CMCDProperty<Int>,
    var bl: CMCDProperty<Int>,
    var bs: CMCDProperty<Boolean>,
    var cid: CMCDProperty<String>,
    var d: CMCDProperty<Int>,
    var dl: CMCDProperty<Int>,
    var mtp: CMCDProperty<Int>,
    var nor: CMCDProperty<String>,
    var ot: CMCDProperty<CMCDObjectType>,
    var pr: CMCDProperty<Float>,
    var rtp: CMCDProperty<Int>,
    var sf: CMCDProperty<CMCDStreamingFormat>,
    var sid: CMCDProperty<String>,
    var st: CMCDProperty<CMCDStreamType>,
    var su: CMCDProperty<Boolean>,
    var tb: CMCDProperty<Int>,
    var v: CMCDProperty<Int>,
) {}

fun <T, TValue> map(properties: MutableMap<String, TValue?>, naming: (String) -> String): ReadWriteProperty<T, TValue?> {
    return object : ReadWriteProperty<T, TValue?> {
        override fun setValue(thisRef: T, property: KProperty<*>, value: TValue?) {
            properties[property.name] = value
        }

        override fun getValue(thisRef: T, property: KProperty<*>) = properties[naming(property.name)]
    }
}

object CamelToHyphen : (String)->String {
    override fun invoke(camelCase: String): String {
        return ""
    }
}

fun <T, TValue> T.versionProperties(properties: MutableMap<String,TValue>) = map(properties, CamelToHyphen)

class MyClass {
    val properties = mutableMapOf<String, Int>()
    val fontSize: String by versionProperties(properties)
}

//expect object CMCDLibraryFactory {
//    fun createCMCDManager(): CMCDManager
//}
// Manager<Version1>()
// manager.setProperty(Version1Keys.bl, 5)

// var manager = Manager<Version2>()
// manager.setProperty(Version2Keys.bs, "Hello")

fun whatever() {

}

