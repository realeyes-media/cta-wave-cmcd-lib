package tech.ctawave.cmcd

import tech.ctawave.cmcd.models.CMCDConfig
import tech.ctawave.cmcd.models.CMCDPayload
import tech.ctawave.cmcd.models.CMCDObjectType

interface CMCDManager {
    var bufferLength: CMCDPayload.BufferLength
    var encodedBitrate: CMCDPayload.EncodedBitrate
    var bufferStarvation: CMCDPayload.BufferStarvation
    var contentId: CMCDPayload.ContentId
    var objectDuration: CMCDPayload.ObjectDuration
    var deadline: CMCDPayload.Deadline
    var measuredThroughput: CMCDPayload.MeasuredThroughput
    var nextObjectRequest: CMCDPayload.NextObjectRequest
    var nextRangeRequest: CMCDPayload.NextRangeRequest
    var objectType: CMCDPayload.ObjectType
    var playbackRate: CMCDPayload.PlaybackRate
    var requestedMaximumThroughput: CMCDPayload.RequestedMaximumThroughput
    var sessionId: CMCDPayload.SessionId
    var streamingFormat: CMCDPayload.StreamingFormat
    var startup: CMCDPayload.Startup
    var streamType: CMCDPayload.StreamType
    var topBitrate: CMCDPayload.TopBitrate
    var version: CMCDPayload.Version

    val debug: Boolean

    fun appendQueryParamsToUrl(url: String, objectType: CMCDObjectType, startup: Boolean = false): String
    fun validate(queryParams: String): Boolean
}

expect object CMCDManagerFactory {
    fun createCMCDManager(config: CMCDConfig): CMCDManager
}
