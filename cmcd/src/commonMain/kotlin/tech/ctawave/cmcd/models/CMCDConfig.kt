package tech.ctawave.cmcd.models

import tech.ctawave.cmcd.CMCDUtil

data class CMCDConfig(val contentId: String, val streamingFormat: CMCDStreamingFormat, val sessionId: String = CMCDUtil.generateUUID(), val debug: Boolean = false, val version: CMCDVersion = CMCDVersion.VERSION_1)

