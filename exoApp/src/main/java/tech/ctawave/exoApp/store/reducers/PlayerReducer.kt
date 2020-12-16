package tech.ctawave.exoApp.store.reducers

import tech.ctawave.exoApp.store.actions.PlayerActions
import tech.ctawave.exoApp.store.state.PlayerState

fun playerReducer(state: PlayerState, action: Any): PlayerState {
    return when (action) {
        is PlayerActions.UpdateCurrentBitrate -> state.copy(currentBitrate = action.bitrate)
        is PlayerActions.UpdateCurrentPlaylistUri -> state.copy(currentPlaylistUri = action.uri)
        is PlayerActions.UpdateCurrentBuffer -> state.copy(currentBuffer = action.buffer)
        is PlayerActions.UpdateCurrentMeasuredThroughput -> state.copy(currentMeasuredThroughput =  action.measuredThroughput)
        is PlayerActions.UpdateCurrentPlaybackRate -> state.copy(currentPlaybackRate = action.playbackRate)
        is PlayerActions.UpdateStreamType -> state.copy(currentStreamType = action.streamType)
        is PlayerActions.UpdateTopBitrate -> state.copy(topBitrate = action.topBitrate)
        is PlayerActions.UpdateCurrentPlayhead -> state.copy(currentPlayhead = action.playhead)
        is PlayerActions.UpdateNextObjectRequest -> state.copy(nextObjectRequest = action.nextObjectRequest)
        is PlayerActions.UpdateNextRangeRequest -> state.copy(nextRangeRequest = action.nextRangeRequest)
        is PlayerActions.UpdateCurrentFramerate -> state.copy(currentFramerate = action.framerate)
        else -> state
    }
}
