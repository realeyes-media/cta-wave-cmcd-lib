import models.CMCDConfig

actual object CMCDManagerFactory {
    actual fun createCMCDManager(config: CMCDConfig): CMCDManager = CMCDManagerCommonFactory.createCMCDManager(config)
}

external fun encodeURIComponent(str: String): String

actual object CMCDUrlEncoder {
    actual fun encode(str: String): String {
        return encodeURIComponent(str)
    }
}
