package tech.ctawave.cmcd

import kotlin.js.ExperimentalJsExport

@ExperimentalJsExport
actual object CMCDManagerFactory {
    actual fun  createCMCDManager(config: CMCDConfig): CMCDManager = CMCDManagerCommonFactory.createCMCDManager(config)
}
