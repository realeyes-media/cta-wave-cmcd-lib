package tech.ctawave.exoApp.store.actions

import tech.ctawave.exoApp.data.entities.StreamType

sealed class PlayerActions {
    data class UpdateCurrentBitrate(val bitrate: Int)
    data class UpdateCurrentPlaylistUri(val uri: String)
    data class UpdateCurrentBuffer(val buffer: Long)
    data class UpdateCurrentMeasuredThroughput(val measuredThroughput: Int)
    data class UpdateCurrentPlaybackRate(val playbackRate: Double)
    data class UpdateStreamType(val streamType: StreamType)
    data class UpdateTopBitrate(val topBitrate: Int)
    data class UpdateCurrentPlayhead(val playhead: Long)
    data class UpdateNextObjectRequest(val nextObjectRequest: String)
    data class UpdateNextRangeRequest(val nextRangeRequest: String)
    data class UpdateCurrentFramerate(val framerate: Float)
}
