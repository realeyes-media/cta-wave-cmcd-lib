import java.net.URLEncoder

actual object CMCDManagerFactory {
    actual fun  createCMCDManager(contentId: String, sessionId: String, streamingFormat: CMCDStreamingFormat, version: CMCDVersion): CMCDManager =
        CMCDManagerCommonFactory.createCMCDManager(contentId, sessionId, streamingFormat, version)
}

actual object CMCDUrlEncoder {
    actual fun encode(str: String): String {
        return URLEncoder.encode(str, "utf-8")
    }
}
