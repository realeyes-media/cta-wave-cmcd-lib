// NOTE (ben.toofer@realeyes.com): Look into using the delagate pattern
// https://proandroiddev.com/dont-reinvent-the-wheel-delegate-it-eac132f2aa64
// https://kotlinlang.org/docs/reference/delegated-properties.html
// This was a suggestion form the kotlin slack channel

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

enum class Version2Keys {
    temp
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
    var br: CMCDProperty<Int>? = map[Version1Keys.br.name] as CMCDProperty<Int>
    var bl: CMCDProperty<Int>? = map[Version1Keys.bl.name] as? CMCDProperty<Int>
    var bs: CMCDProperty<Boolean>? = map[Version1Keys.bs.name] as? CMCDProperty<Boolean>
    var cid: CMCDProperty<String>? = map[Version1Keys.cid.name] as? CMCDProperty<String>
    var d: CMCDProperty<Int>? = map[Version1Keys.d.name] as? CMCDProperty<Int>
    var dl: CMCDProperty<Int>? = map[Version1Keys.dl.name] as? CMCDProperty<Int>
    var mtp: CMCDProperty<Int>? = map[Version1Keys.mtp.name] as? CMCDProperty<Int>
    var nor: CMCDProperty<String>? = map[Version1Keys.nor.name] as? CMCDProperty<String>
    var ot: CMCDProperty<CMCDObjectType>? = map[Version1Keys.ot.name] as? CMCDProperty<CMCDObjectType>
    var pr: CMCDProperty<Float>? = map[Version1Keys.pr.name] as? CMCDProperty<Float>
    var rtp: CMCDProperty<Int>? = map[Version1Keys.rtp.name] as? CMCDProperty<Int>
    var sf: CMCDProperty<CMCDStreamingFormat>? =
        map[Version1Keys.sf.name] as? CMCDProperty<CMCDStreamingFormat>
    var sid: CMCDProperty<String>? = map[Version1Keys.sid.name] as? CMCDProperty<String>
    var st: CMCDProperty<CMCDStreamType>? = map[Version1Keys.st.name] as? CMCDProperty<CMCDStreamType>
    var su: CMCDProperty<Boolean>? = map[Version1Keys.su.name] as? CMCDProperty<Boolean>
    var tb: CMCDProperty<Int>? = map[Version1Keys.tb.name] as? CMCDProperty<Int>
    var v: CMCDProperty<Int>? = map[Version1Keys.v.name] as? CMCDProperty<Int>
}

class Version2(map: MutableMap<String, CMCDProperty<*>>) : Version(map) {

}

enum class VersionLibrary {
    VERSION_1,
    VERSION_2,
    VERSION_3,
}

class CMCDManagerTest(val version: Version) {
    val queryParamMap: MutableMap<String, Any> = mutableMapOf()
    fun setBitrate(key: String, value: Int) {
        setProperty(key, value)
    }

    fun setBufferLength(key: String, value: Int) {
        setProperty(key, value)
    }

    fun getQueryParams() = queryParamMap

    private fun <T> setProperty(key: String, value: T) {
        queryParamMap.put(key, value!!)
    }
}

class CMCDManagerFactoryFake {
    fun createCMCDManager(versionLibrary: VersionLibrary): CMCDManagerTest {
        val versionObj: Version = when (versionLibrary) {
            VersionLibrary.VERSION_1 -> Version1(
                mutableMapOf(
                    Version1Keys.br.name to CMCDProperty(CMCDHeaderName.Object, "", 256),
                    Version1Keys.bl.name to CMCDProperty(CMCDHeaderName.Object, "", 5),
                    Version1Keys.d.name to CMCDProperty(CMCDHeaderName.Object, "", 10),
                )
            )

            VersionLibrary.VERSION_2 -> Version2(mutableMapOf())
            else -> Version1(mutableMapOf())
        }

        return CMCDManagerTest(versionObj)
    }
}

@ExperimentalStdlibApi
fun testing() {
    val factoryFake = CMCDManagerFactoryFake()
    val manager = factoryFake.createCMCDManager(VersionLibrary.VERSION_1)
    manager.setBitrate(Version1Keys.br.name, 1)
    manager.setBufferLength(Version1Keys.bl.name, 5)
}

