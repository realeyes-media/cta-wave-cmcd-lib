object CMCDManagerCommonFactory {
    fun createCMCDManager(contentId: String, sessionId: String, streamingFormat: CMCDStreamingFormat, versionLibrary: CMCDVersion): CMCDManagerCommon {
        val versionObj: Version = when (versionLibrary) {
            CMCDVersion.VERSION_1 -> Version(
                mutableMapOf(
                    CMCDPayloadKey.Version1(Version1Key.BUFFER_LENGTH) to CMCDProperty<Int>(Version1Key.BUFFER_LENGTH.header, null),
                    CMCDPayloadKey.Version1(Version1Key.ENCODED_BITRATE) to CMCDProperty<Int>(Version1Key.ENCODED_BITRATE.header, null),
                    CMCDPayloadKey.Version1(Version1Key.BUFFER_STARVATION) to CMCDProperty(Version1Key.BUFFER_STARVATION.header, false),
                    CMCDPayloadKey.Version1(Version1Key.CONTENT_ID) to CMCDProperty(Version1Key.CONTENT_ID.header, "\"$contentId\""),
                    CMCDPayloadKey.Version1(Version1Key.OBJECT_DURATION) to CMCDProperty<Int>(Version1Key.OBJECT_DURATION.header, null),
                    CMCDPayloadKey.Version1(Version1Key.DEADLINE) to CMCDProperty<Int>(Version1Key.DEADLINE.header, null),
                    CMCDPayloadKey.Version1(Version1Key.MEASURED_THROUGHPUT) to CMCDProperty<Int>(Version1Key.MEASURED_THROUGHPUT.header, null),
                    CMCDPayloadKey.Version1(Version1Key.NEXT_OBJECT_REQUEST) to CMCDProperty<String>(Version1Key.NEXT_OBJECT_REQUEST.header, null),
                    CMCDPayloadKey.Version1(Version1Key.NEXT_RANGE_REQUEST) to CMCDProperty<String>(Version1Key.NEXT_RANGE_REQUEST.header, null),
                    CMCDPayloadKey.Common(CommonKey.OBJECT_TYPE) to CMCDProperty<CMCDObjectType>(CommonKey.OBJECT_TYPE.header, null),
                    CMCDPayloadKey.Version1(Version1Key.PLAYBACK_RATE) to CMCDProperty(Version1Key.PLAYBACK_RATE.header, 0.0),
                    CMCDPayloadKey.Version1(Version1Key.REQUESTED_MAXIMUM_THROUGHPUT) to CMCDProperty<Int>(Version1Key.REQUESTED_MAXIMUM_THROUGHPUT.header, null),
                    CMCDPayloadKey.Common(CommonKey.STREAMING_FORMAT) to CMCDProperty(CommonKey.STREAMING_FORMAT.header, streamingFormat.value),
                    CMCDPayloadKey.Version1(Version1Key.SESSION_ID) to CMCDProperty(Version1Key.SESSION_ID.header, "\"$sessionId\""),
                    CMCDPayloadKey.Version1(Version1Key.STREAM_TYPE) to CMCDProperty<CMCDStreamType>(Version1Key.STREAM_TYPE.header, null),
                    CMCDPayloadKey.Common(CommonKey.STARTUP) to CMCDProperty(CommonKey.STARTUP.header, false),
                    CMCDPayloadKey.Version1(Version1Key.TOP_BITRATE) to CMCDProperty<Int>(Version1Key.TOP_BITRATE.header, null),
                )
            )
        }

        return CMCDManagerCommon(versionObj)
    }
}

class CMCDManagerCommon(private var version: Version): CMCDManager {
    override fun <T> setProperty(key: CMCDPayloadKey, value: T) {
        if (version.map.containsKey(key)) {
            val property = CMCDProperty(key.cmcdKey.header, value)
            version.map[key] = property
        }
    }

    override fun appendQueryParamsToUrl(url: String, objectType: CMCDObjectType, startup: Boolean): String {
        version.map[CMCDPayloadKey.Common(CommonKey.OBJECT_TYPE)] = CMCDProperty(CommonKey.OBJECT_TYPE.header, objectType.value)
        version.map[CMCDPayloadKey.Common(CommonKey.STARTUP)] = CMCDProperty(CommonKey.STARTUP.header, startup)

        val delimiter = if (url.contains("?")) "&" else "?"
        val keyValues = buildKeyValues()

        val keyValuesStr = keyValues.map { entry ->
            val value = entry.value ?: ""
            val equal = if (value.isNotEmpty()) "%3D" else ""
            "${entry.key.cmcdKey.key}$equal$value"
        }.joinToString("%2C")

        println("$$$ delimiter: $delimiter")
        println("$$$ keyValuesStr: $keyValuesStr")

        val queryParams = if (keyValuesStr.isNotEmpty()) "${delimiter}CMCD=$keyValuesStr" else ""

        println("$$$ url: $url")

        return "$url$queryParams"
    }

    override fun validate(queryParams: String): Boolean {
        var result = true

        // if queryParams contains CMCD=, strip it
        // lowercase for all string comparison
        val str = queryParams.replace("CMCD=", "").toLowerCase()

        val map = str.split("%2c").associate {
            val (left, right) = it.split("%3d")
            left to right
        }

        map.forEach { (key, value) ->
            run {
                if (!CMCDPayloadKey.matchingValueForKey(key, value)) {
                    result = false
                    return@forEach
                }
            }
        }

        return result
    }

    private fun buildKeyValues(): MutableMap<CMCDPayloadKey, String?> {
        val result = mutableMapOf<CMCDPayloadKey, String?>()
        version.map.forEach { entry ->
            val property = entry.value
            property.value?.let {
                println("$$$ key: ${entry.key.cmcdKey.key}, value: $it")

                if (it != false) {
                    val keyValue = if (it is String) CMCDUrlEncoder.encode(it) else "$it"
                    println("$$$ key: ${entry.key.cmcdKey.key}, keyValue: $keyValue")
                    if (CMCDPayloadKey.matchingValueForKey(entry.key.cmcdKey.key, keyValue)) {
                        result[entry.key] = if (it == true) null else keyValue
                    }
                }
            }
        }
        return result
    }
}
