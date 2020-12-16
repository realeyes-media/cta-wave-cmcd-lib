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
        println("$$$ cmcd > init")
        cmcdManager = CMCDManagerFactory.createCMCDManager(config)
    }

    fun createCMCDCompliantUri(uri: String, objectType: CMCDObjectType, startup: Boolean = false): String {
        return cmcdManager?.appendQueryParamsToUrl(uri, objectType, startup) ?: uri
    }

    fun updateBufferLength(bufferLength: Int) {
        println("$$$ cmcd > updateBufferLength: $bufferLength")
        cmcdManager?.bufferLength = CMCDPayload.BufferLength(bufferLength)
    }

    fun updateEncodedBitrate(encodedBitrate: Int) {
        println("$$$ cmcd > updateEncodedBitrate: $encodedBitrate")
        cmcdManager?.encodedBitrate = CMCDPayload.EncodedBitrate(encodedBitrate)
    }

    fun updateBufferStarvation(bufferStarvation: Boolean) {
        println("$$$ cmcd > updateBufferStarvation: $bufferStarvation")
        cmcdManager?.bufferStarvation = CMCDPayload.BufferStarvation(bufferStarvation)
    }

    fun updateObjectDuration(objectDuration: Int) {
        println("$$$ cmcd > objectDuration: $objectDuration")
        cmcdManager?.objectDuration = CMCDPayload.ObjectDuration(objectDuration)
    }

    fun updateDeadline(deadline: Int) {
        println("$$$ cmcd > updateDeadline: $deadline")
        cmcdManager?.deadline = CMCDPayload.Deadline(deadline)
    }

    fun updateMeasuredThroughput(measuredThroughput: Int) {
        println("$$$ cmcd > updateMeasuredThroughput: $measuredThroughput")
        cmcdManager?.measuredThroughput = CMCDPayload.MeasuredThroughput(measuredThroughput)
    }

    fun updateNextObjectRequest(nextObjectRequest: String) {
        println("$$$ cmcd > updateNextObjectRequest: $nextObjectRequest")
        cmcdManager?.nextObjectRequest = CMCDPayload.NextObjectRequest(nextObjectRequest)
    }

    fun updateNextRangeRequest(nextRangeRequest: String) {
        println("$$$ cmcd > updateNextRangeRequest: $nextRangeRequest")
        cmcdManager?.nextRangeRequest = CMCDPayload.NextRangeRequest(nextRangeRequest)
    }

    fun updatePlaybackRate(playbackRate: Double) {
        println("$$$ cmcd > updatePlaybackRate: $playbackRate")
        cmcdManager?.playbackRate = CMCDPayload.PlaybackRate(playbackRate)
    }

    fun updateRequestedMaximumThroughput(requestedMaximumThroughput: Int) {
        println("$$$ cmcd > updateRequestedMaximumThroughput: $requestedMaximumThroughput")
        cmcdManager?.requestedMaximumThroughput = CMCDPayload.RequestedMaximumThroughput(requestedMaximumThroughput)
    }

    fun updateStreamType(streamType: StreamType) {
        val cmcdStreamType = when (streamType) {
            StreamType.LIVE -> CMCDStreamType.LIVE
            StreamType.VOD -> CMCDStreamType.VOD
        }
        println("$$$ cmcd > updateStreamType: $cmcdStreamType")
        cmcdManager?.streamType = CMCDPayload.StreamType(cmcdStreamType)
    }

    fun updateTopBitrate(topBitrate: Int) {
        println("$$$ cmcd > updateTypBitrate: $topBitrate")
        cmcdManager?.topBitrate = CMCDPayload.TopBitrate(topBitrate)
    }
}
