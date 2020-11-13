class CMCDManagerCommon(val version: Version) {
    val queryParamMap: MutableMap<String, Any?> = mutableMapOf()
    fun setBitrate(key: String, value: Int?) {
        setProperty(key, value)
    }

    fun setBufferLength(key: String, value: Int) {
        setProperty(key, value)
    }

    fun setStreamingFormat(key: String, value: CMCDStreamingFormat) {
        setProperty(key, value)
    }

    fun validate(queryParams: String): Boolean {
        var result = true
        val map = queryParams.split(",").associate {
            val (left, right) = it.split("=")
            left to right
        }
        map.forEach { (key, value) ->
            run {
                if (!queryParamMap.containsKey(key) || !isMatchingValueType(key, value)) {
                    result = false
                    return@forEach
                }
            }
        }
        return result
    }

    private fun isMatchingValueType(key: String, value: String?): Boolean {
        try {
            when {
                queryParamMap[key] is Int -> {
                    value?.toInt()
                }
                queryParamMap[key] is String -> {
                    value.toString()
                }
                queryParamMap[key] is Long -> {
                    value?.toLong()
                }
                queryParamMap[key] is Double -> {
                    value?.toDouble()
                }
                queryParamMap[key] is Float -> {
                    value?.toFloat()
                }
                queryParamMap[key] is Boolean -> {
                    value.toBoolean()
                }
                queryParamMap[key] is CMCDStreamingFormat -> {
                    value?.let { CMCDStreamingFormat.valueOf(it) }
                }
                queryParamMap[key] is CMCDStreamType -> {
                    value?.let { CMCDStreamType.valueOf(it) }
                }
            }
            return true
        } catch (exception: Exception) {
            return false
        }
    }

    fun getQueryParams() = queryParamMap.toString()

    private fun <T> setProperty(key: String, value: T) {
        if (version.map.containsKey(key))
            queryParamMap.put(key, value)
    }
}
