interface CMCDManager {
    fun <T> setProperty(key: CMCDPayloadKey, value: T)
    fun appendQueryParamsToUrl(url: String, objectType: CMCDObjectType, startup: Boolean = false): String
    fun validate(queryParams: String): Boolean
}

expect object CMCDManagerFactory {
    fun createCMCDManager(contentId: String, sessionId: String, streamingFormat: CMCDStreamingFormat, version: CMCDVersion = CMCDVersion.VERSION_1): CMCDManager
}

expect object CMCDUrlEncoder {
    fun encode(str: String): String
}
