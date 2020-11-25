import models.CMCDConfig
import java.net.URLEncoder

actual object CMCDManagerFactory {
    actual fun  createCMCDManager(config: CMCDConfig): CMCDManager = CMCDManagerCommonFactory.createCMCDManager(config)
}

actual object CMCDUrlEncoder {
    actual fun encode(str: String): String {
        return URLEncoder.encode(str, "utf-8")
    }
}
