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
    enum class Version2Keys {
        temp
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




/**
 * Manager<V : VersionKeys> {
 *  setProperty(key: V, value: T[V])
 *  getProperty(key: V): T[V]
 * }
 */
//expect object CMCDLibraryFactory {
//    fun createCMCDManager(): CMCDManager
//}
// Manager<Version1Keys>()
// manager.setProperty(Version1Keys.bl, 5)
// manager.setProperty(Version1Keys.cid, 6)

// var manager = Manager<Version2>()
// manager.setProperty(Version2Keys.bs, "Hello")

data class CMCDProperty<T>( val header: CMCDHeaderName, val description: String, var value: T) {}

abstract class Version<C: Enum<C>>(map : Map<Enum<C>, CMCDProperty<*>>)

typealias Version1Structure = Map<Enum<VersionKeys.Version1Keys>, CMCDProperty<*>>
typealias Version2Structure = Map<Enum<VersionKeys.Version2Keys>, CMCDProperty<*>>

class Version1(map: Version1Structure): Version<VersionKeys.Version1Keys>(map) {
    var br: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.br) as? CMCDProperty<Int>
    var bl: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.br) as? CMCDProperty<Int>
    var bs: CMCDProperty<Boolean>? = map.get(VersionKeys.Version1Keys.bs) as? CMCDProperty<Boolean>
    var cid: CMCDProperty<String>? = map.get(VersionKeys.Version1Keys.cid) as? CMCDProperty<String>
    var d: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.d) as? CMCDProperty<Int>
    var dl: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.dl) as? CMCDProperty<Int>
    var mtp: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.mtp) as? CMCDProperty<Int>
    var nor: CMCDProperty<String>? = map.get(VersionKeys.Version1Keys.nor) as? CMCDProperty<String>
    var ot: CMCDProperty<CMCDObjectType>? = map.get(VersionKeys.Version1Keys.ot) as? CMCDProperty<CMCDObjectType>
    var pr: CMCDProperty<Float>? = map.get(VersionKeys.Version1Keys.pr) as? CMCDProperty<Float>
    var rtp: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.rtp) as? CMCDProperty<Int>
    var sf: CMCDProperty<CMCDStreamingFormat>? = map.get(VersionKeys.Version1Keys.sf) as? CMCDProperty<CMCDStreamingFormat>
    var sid: CMCDProperty<String>? = map.get(VersionKeys.Version1Keys.sid) as? CMCDProperty<String>
    var st: CMCDProperty<CMCDStreamType>? = map.get(VersionKeys.Version1Keys.st) as? CMCDProperty<CMCDStreamType>
    var su: CMCDProperty<Boolean>? = map.get(VersionKeys.Version1Keys.su) as? CMCDProperty<Boolean>
    var tb: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.tb) as? CMCDProperty<Int>
    var v: CMCDProperty<Int>? = map.get(VersionKeys.Version1Keys.v) as? CMCDProperty<Int>
}

class Version2(map: Version2Structure): Version<VersionKeys.Version2Keys>(map) {

}

enum class VersionLibrary {
    VERSION_1,
    VERSION_2,
    VERSION_3,
}
class CMCDManagerTest<C : Enum<C>> {

    val version: Version<C>

    constructor(version: Version<C>) {
        this.version = version
    }

    fun setProperty(key: C, value: Any) {
        this.version
    }
}

class CMCDManagerFactoryFake {
    fun <C: Enum<C>> createCMCDManager(versionLibrary: VersionLibrary): CMCDManagerTest<C> {
        var versionObj: Version<*>
        when(versionLibrary){
            VersionLibrary.VERSION_1 -> versionObj = Version1(emptyMap())
            VersionLibrary.VERSION_2 -> versionObj = Version2(emptyMap())
            else -> versionObj = Version1(emptyMap())
        }

        return CMCDManagerTest<C>(versionObj)
    }
}



fun testing() {
    val factoryFake = CMCDManagerFactoryFake()
    val manager = factoryFake.createCMCDManager<VersionKeys.Version1Keys>(VersionLibrary.VERSION_1)
    manager.setProperty(VersionKeys.Version1Keys.br, "")
}


/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

data class Version1_TEMP (
    val map: Map<String, CMCDProperty<Any>>
) {
    fun getValue(key: String): CMCDProperty<Any>? {
        // nullable and untyped
        return map[key]
    }
}

enum class CMCDKey {
    BR_KEY, BL_KEY, BS_KEY, CID_KEY
}
data class Version3 (
    val keyProperties: List<CMCDKeyProperty<Any>>
) {
    fun <T : Any> getValue(key: CMCDTypedKey<T>): CMCDProperty<T>? {
        // still nullable but typed...
        // and the when is exhaustive without an else...
        return keyProperties.find { key == it.key } as? CMCDProperty<T>
    }
}
sealed class CMCDTypedKey<T : Any> {
    object BR_KEY : CMCDTypedKey<Int>()
    object BL_KEY : CMCDTypedKey<Int>()
    object BS_KEY : CMCDTypedKey<Boolean>()
    object CID_KEY : CMCDTypedKey<String>()
}
sealed class CMCDKeyProperty<T : Any> {
    abstract val key: CMCDTypedKey<T>
    abstract val property : T
}
class CMCDKeyIntProperty(override val key: CMCDTypedKey<Int>, override val property: Int) : CMCDKeyProperty<Int>()
class CMCDKeyBoolProperty(override val key: CMCDTypedKey<Boolean>, override val property: Boolean) : CMCDKeyProperty<Boolean>()
class CMCDKeyStringProperty(override val key: CMCDTypedKey<String>, override val property: String) : CMCDKeyProperty<String>()

