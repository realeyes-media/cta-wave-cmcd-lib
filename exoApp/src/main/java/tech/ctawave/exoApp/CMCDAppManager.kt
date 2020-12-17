package tech.ctawave.exoApp

import tech.ctawave.cmcd.CMCDConfig
import tech.ctawave.cmcd.CMCDManager
import tech.ctawave.cmcd.CMCDManagerFactory
import tech.ctawave.cmcd.models.CMCDObjectType
import tech.ctawave.cmcd.models.CMCDStreamType
import tech.ctawave.cmcd.models.CMCDStreamingFormat
import tech.ctawave.exoApp.data.entities.StreamType
import tech.ctawave.exoApp.data.entities.StreamingFormat
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.js.ExperimentalJsExport

@ExperimentalJsExport
@Singleton
class CMCDAppManager @Inject constructor() {
    private var cmcdManager: CMCDManager? = null

    fun initForContentId(contentId: String, streamingFormat: StreamingFormat) {
        val sf = when (streamingFormat) {
            StreamingFormat.HLS -> CMCDStreamingFormat.HLS
            StreamingFormat.MPEG_DASH -> CMCDStreamingFormat.MPEG_DASH
            StreamingFormat.SMOOTH_STREAMING -> CMCDStreamingFormat.SMOOTH_STREAMING
        }
        val config = CMCDConfig(contentId, sf.token)
        println("$$$ cmcd > init")
        cmcdManager = CMCDManagerFactory.createCMCDManager(config)
    }

    fun createCMCDCompliantUri(uri: String, objectType: CMCDObjectType, startup: Boolean = false): String {
        return cmcdManager?.appendQueryParamsToUri(uri, objectType.token, startup) ?: uri
    }

    fun updateBufferLength(bufferLength: Long) {
        println("$$$ cmcd > updateBufferLength: $bufferLength")
        cmcdManager?.bufferLength = bufferLength.toInt()
    }

    fun updateEncodedBitrate(encodedBitrate: Int) {
        println("$$$ cmcd > updateEncodedBitrate: $encodedBitrate")
        cmcdManager?.encodedBitrate = encodedBitrate
    }

    fun updateBufferStarvation(bufferStarvation: Boolean) {
        println("$$$ cmcd > updateBufferStarvation: $bufferStarvation")
        cmcdManager?.bufferStarvation = bufferStarvation
    }

    fun updateObjectDuration(objectDuration: Int) {
        println("$$$ cmcd > objectDuration: $objectDuration")
        cmcdManager?.objectDuration = objectDuration
    }

    fun updateDeadline(deadline: Long) {
        println("$$$ cmcd > updateDeadline: $deadline")
        cmcdManager?.deadline = deadline.toInt()
    }

    fun updateMeasuredThroughput(measuredThroughput: Long) {
        println("$$$ cmcd > updateMeasuredThroughput: $measuredThroughput")
        cmcdManager?.measuredThroughput = measuredThroughput.toInt()
    }

    fun updateNextObjectRequest(nextObjectRequest: String) {
        println("$$$ cmcd > updateNextObjectRequest: $nextObjectRequest")
        cmcdManager?.nextObjectRequest = nextObjectRequest
    }

    fun updateNextRangeRequest(nextRangeRequest: String) {
        println("$$$ cmcd > updateNextRangeRequest: $nextRangeRequest")
        cmcdManager?.nextRangeRequest = nextRangeRequest
    }

    fun updatePlaybackRate(playbackRate: Double) {
        println("$$$ cmcd > updatePlaybackRate: $playbackRate")
        cmcdManager?.playbackRate = playbackRate
    }

    fun updateRequestedMaximumThroughput(requestedMaximumThroughput: Int) {
        println("$$$ cmcd > updateRequestedMaximumThroughput: $requestedMaximumThroughput")
        cmcdManager?.requestedMaximumThroughput = requestedMaximumThroughput
    }

    fun updateStreamType(streamType: StreamType) {
        val cmcdStreamType = when (streamType) {
            StreamType.LIVE -> CMCDStreamType.LIVE
            StreamType.VOD -> CMCDStreamType.VOD
        }
        println("$$$ cmcd > updateStreamType: $cmcdStreamType")
        cmcdManager?.streamType = cmcdStreamType.token
    }

    fun updateTopBitrate(topBitrate: Int) {
        println("$$$ cmcd > updateTypBitrate: $topBitrate")
        cmcdManager?.topBitrate = topBitrate
    }
}
