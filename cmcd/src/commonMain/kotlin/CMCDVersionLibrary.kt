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

abstract class Version<C:IVersionKeys>(map : Map<C, CMCDProperty<*>>)

typealias Version1Structure = Map<IVersionKeys, CMCDProperty<*>>
typealias Version2Structure = Map<IVersionKeys, CMCDProperty<*>>

class Version1(map: Version1Structure): Version<IVersionKeys>(map) {
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

class Version2(map: Version2Structure): Version<IVersionKeys>(map) {

}

enum class VersionLibrary {
    VERSION_1,
    VERSION_2,
    VERSION_3,
}
interface IVersionKeys
interface IVersion1Keys: IVersionKeys
sealed class V1 {
    enum class IntKeys: IVersion1Keys {
        TEMP,
        SOME
    }

    enum class StringKeys: IVersion1Keys {
        WHATEVER
    }
}



class CMCDManagerTest<C : IVersionKeys> {

    val version: Version<C>

    constructor(version: Version<C>) {
        this.version = version
    }

    fun setProperty(key: IntKeys, value: Int) {
        this.version
    }

    fun setProperty(key: StringKeys, value: String) {
        this.version
    }
}

class CMCDManagerFactoryFake {
    fun <C: IVersionKeys>createCMCDManager(versionLibrary: VersionLibrary): CMCDManagerTest<C> {
        var versionObj: Version<*>
        when(versionLibrary){
            VersionLibrary.VERSION_1 -> versionObj = Version1(mapOf(
                IntKeys.SOME to CMCDProperty<Int>(CMCDHeaderName.Object, "", 5),
                IntKeys.TEMP to CMCDProperty<Int>(CMCDHeaderName.Object, "", 5),
                StringKeys.WHATEVER to CMCDProperty<Int>(CMCDHeaderName.Object, "", 5),
                StringKeys.WHATEVER to CMCDProperty<Int>(CMCDHeaderName.Object, "", 5),
            ))
            VersionLibrary.VERSION_2 -> versionObj = Version2(emptyMap())
            else -> versionObj = Version1(emptyMap())
        }

        return CMCDManagerTest<C>(versionObj as Version<C>)
    }
}



fun testing() {
    var ben = Ben()
    ben.
//    var temp: Enum<VersionKeys.Version1Keys> = setOf(VersionKeys.Version1Keys.br, VersionKeys.Version1Keys.bl)
    val factoryFake = CMCDManagerFactoryFake()
    val manager = factoryFake.createCMCDManager<IVersion1Keys>(VersionLibrary.VERSION_1)
    manager.setProperty(IntKeys.SOME, 5)
    manager.setProperty(StringKeys.WHATEVER, "")
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

