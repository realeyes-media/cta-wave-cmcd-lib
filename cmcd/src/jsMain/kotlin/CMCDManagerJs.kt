actual object CMCDManagerFactory {
    actual fun  createCMCDManager(contentId: String, sessionId: String, streamingFormat: CMCDStreamingFormat, version: CMCDVersion): CMCDManager =
        CMCDManagerCommonFactory.createCMCDManager(contentId, sessionId, streamingFormat, version)
}

external fun encodeURIComponent(str: String): String

actual object CMCDUrlEncoder {
    actual fun encode(str: String): String {
        return encodeURIComponent(str)
    }
}
