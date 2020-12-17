package tech.ctawave.cmcd

@ExperimentalJsExport
@JsExport
actual object CMCDManagerFactory {
    actual fun createCMCDManager(config: CMCDConfig): CMCDManager = CMCDManagerCommonFactory.createCMCDManager(config)
}
