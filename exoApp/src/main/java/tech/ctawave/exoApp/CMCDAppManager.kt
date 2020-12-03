package tech.ctawave.exoApp

import tech.ctawave.cmcd.CMCDManager
import tech.ctawave.cmcd.CMCDManagerFactory
import tech.ctawave.cmcd.models.CMCDConfig
import tech.ctawave.cmcd.models.CMCDObjectType
import tech.ctawave.cmcd.models.CMCDPayload
import tech.ctawave.cmcd.models.CMCDStreamType
import tech.ctawave.cmcd.models.CMCDStreamingFormat
import tech.ctawave.exoApp.data.entities.StreamType
import tech.ctawave.exoApp.data.entities.StreamingFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CMCDAppManager @Inject constructor() {
    private var cmcdManager: CMCDManager? = null

    fun initForContentId(contentId: String, streamingFormat: StreamingFormat, debug: Boolean = true) {
        val sf = when (streamingFormat) {
            StreamingFormat.HLS -> CMCDStreamingFormat.HLS
            StreamingFormat.MPEG_DASH -> CMCDStreamingFormat.MPEG_DASH
            StreamingFormat.SMOOTH_STREAMING -> CMCDStreamingFormat.SMOOTH_STREAMING
        }
        val config = CMCDConfig(contentId, sf, debug = debug)
        cmcdManager = CMCDManagerFactory.createCMCDManager(config)
    }

    fun createCMCDCompliantUri(uri: String, objectType: CMCDObjectType, startup: Boolean): String {
        return cmcdManager?.appendQueryParamsToUrl(uri, objectType, startup) ?: uri
    }

    fun updateBufferLength(bufferLength: Int) {
        cmcdManager?.bufferLength = CMCDPayload.BufferLength(bufferLength)
    }

    fun updateEncodedBitrate(encodedBitrate: Int) {
        cmcdManager?.encodedBitrate = CMCDPayload.EncodedBitrate(encodedBitrate)
    }

    fun updateBufferStarvation(bufferStarvation: Boolean) {
        cmcdManager?.bufferStarvation = CMCDPayload.BufferStarvation(bufferStarvation)
    }

    fun updateObjectDuration(objectDuration: Int) {
        cmcdManager?.objectDuration = CMCDPayload.ObjectDuration(objectDuration)
    }

    fun updateDeadline(deadline: Int) {
        cmcdManager?.deadline = CMCDPayload.Deadline(deadline)
    }

    fun updateMeasuredThroughput(measuredThroughput: Int) {
        cmcdManager?.measuredThroughput = CMCDPayload.MeasuredThroughput(measuredThroughput)
    }

    fun updateNextObjectRequest(nextObjectRequest: String) {
        cmcdManager?.nextObjectRequest = CMCDPayload.NextObjectRequest(nextObjectRequest)
    }

    fun updateNextRangeRequest(nextRangeRequest: String) {
        cmcdManager?.nextRangeRequest = CMCDPayload.NextRangeRequest(nextRangeRequest)
    }

    fun updatePlaybackRate(playbackRate: Double) {
        cmcdManager?.playbackRate = CMCDPayload.PlaybackRate(playbackRate)
    }

    fun updateRequestedMaximumThroughput(requestedMaximumThroughput: Int) {
        cmcdManager?.requestedMaximumThroughput = CMCDPayload.RequestedMaximumThroughput(requestedMaximumThroughput)
    }

    fun updateStreamType(streamType: StreamType) {
        val cmcdStreamType = when (streamType) {
            StreamType.LIVE -> CMCDStreamType.LIVE
            StreamType.VOD -> CMCDStreamType.VOD
        }
        cmcdManager?.streamType = CMCDPayload.StreamType(cmcdStreamType)
    }

    fun updateTopBitrate(topBitrate: Int) {
        cmcdManager?.topBitrate = CMCDPayload.TopBitrate(topBitrate)
    }
}
