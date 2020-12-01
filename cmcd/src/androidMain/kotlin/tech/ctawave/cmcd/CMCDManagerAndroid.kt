package tech.ctawave.cmcd

import tech.ctawave.cmcd.models.CMCDConfig

actual object CMCDManagerFactory {
    actual fun  createCMCDManager(config: CMCDConfig): CMCDManager = CMCDManagerCommonFactory.createCMCDManager(config)
}
