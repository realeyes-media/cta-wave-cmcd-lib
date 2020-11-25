package models

data class CMCDConfig(val contentId: String, val sessionId: String, val streamingFormat: CMCDStreamingFormat, val debug: Boolean, val version: CMCDVersion = CMCDVersion.VERSION_1)

