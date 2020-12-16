package tech.ctawave.exoApp.store.state

import tech.ctawave.exoApp.data.entities.StreamType

data class PlayerState(
    val currentBitrate: Int? = null,
    val currentPlaylistUri: String? = null,
    val currentBuffer: Long? = null,
    val currentMeasuredThroughput: Int? = null,
    val currentPlaybackRate: Double? = null,
    val currentStreamType: StreamType? = null,
    val currentFramerate: Float? = null,
    val topBitrate: Int? = null,
    val currentPlayhead: Long? = null,
    val nextObjectRequest: String? = null,
    val nextRangeRequest: String? = null
)
