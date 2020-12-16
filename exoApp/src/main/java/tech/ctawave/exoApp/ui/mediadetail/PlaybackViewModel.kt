package tech.ctawave.exoApp.ui.mediadetail

import android.app.Application
import android.net.Uri
import android.view.Surface
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.hls.HlsManifest
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist
import com.google.android.exoplayer2.upstream.DataSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.ctawave.exoApp.CMCDAppManager
import tech.ctawave.exoApp.data.entities.StreamType
import tech.ctawave.exoApp.data.entities.StreamingFormat
import tech.ctawave.exoApp.store.actions.PlayerActions
import tech.ctawave.exoApp.store.store
import tech.ctawave.exoApp.util.determineCMCDObjectType

class PlaybackViewModel @ViewModelInject constructor(application: Application, private val cmcdAppManager: CMCDAppManager) :
    AndroidViewModel(application),
    Player.EventListener, AnalyticsListener {

    private var exoPlayer: SimpleExoPlayer? = null

    // CMCD
    var contentId: String? = null
    var streamingFormat: StreamingFormat? = null

    private var finishedStartup = false

    private val _lastUri = MutableStateFlow("")
    var lastUri: StateFlow<String> = _lastUri

    private val _currentBuffer = MutableStateFlow(0)
    private val _currentBitrate = MutableStateFlow(0)
    private val _measuredThroughput = MutableStateFlow(0)
    private val _streamType = MutableStateFlow<StreamType?>(null)
    private val _playbackRate = MutableStateFlow(0.0)

    fun setPlayer(exoPlayer: SimpleExoPlayer) {
        this.exoPlayer = exoPlayer
    }

    fun initCMCD() {
        contentId?.let { id ->
            streamingFormat?.let { sf ->
                cmcdAppManager.initForContentId(id, sf)
                observeCMCDProperties()
            }
        }
    }

    fun pollPlayhead() {
        viewModelScope.launch {
            while (true) {
                exoPlayer?.let {
                    updatePlayhead(it)
                    updateCurrentBuffer(it)
                }
                delay(100)
            }
        }
    }

    fun createCMCDCompliantUri(dataSpec: DataSpec): Uri {
        val ot = dataSpec.determineCMCDObjectType()
        val uri = Uri.parse(cmcdAppManager.createCMCDCompliantUri(dataSpec.uri.toString(), ot, !finishedStartup))
        println("$$$ cmcd > uri: $uri")
        _lastUri.value = uri.toString()
        return uri
    }

    override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime, surface: Surface?) {
        super.onRenderedFirstFrame(eventTime, surface)
        exoPlayer?.let {
            val bitrate = it.videoFormat?.bitrate ?: 0
            _currentBitrate.value =  bitrate

            val framerate = it.videoFormat?.frameRate ?: 0f
            store.dispatch(PlayerActions.UpdateCurrentFramerate(framerate))
        }
    }

    override fun onBandwidthEstimate(
        eventTime: AnalyticsListener.EventTime,
        totalLoadTimeMs: Int,
        totalBytesLoaded: Long,
        bitrateEstimate: Long
    ) {
        super.onBandwidthEstimate(eventTime, totalLoadTimeMs, totalBytesLoaded, bitrateEstimate)
        _measuredThroughput.value = bitrateEstimate.toInt()
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        (exoPlayer?.currentManifest as HlsManifest).let { hlsManifest ->
             val streamType = when (hlsManifest.mediaPlaylist.playlistType) {
                 HlsMediaPlaylist.PLAYLIST_TYPE_EVENT -> StreamType.LIVE
                 HlsMediaPlaylist.PLAYLIST_TYPE_VOD -> StreamType.VOD
                 else -> null
             }
            _streamType.value = streamType
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playbackRate.value = if (isPlaying) 1.0 else 0.0

        if (isPlaying) {
            _playbackRate.value = 1.0
            finishedStartup = true
            cmcdAppManager.updateBufferStarvation(false)
        } else {
            _playbackRate.value = 0.0
            exoPlayer?.let {
                when (it.playbackState) {
                    Player.STATE_BUFFERING -> cmcdAppManager.updateBufferStarvation(true)
                    else -> {}
                }
            }
        }
    }

    private fun updatePlayhead(exo: ExoPlayer) {
        if (exo.isPlaying) {
            store.dispatch(PlayerActions.UpdateCurrentPlayhead(exo.currentPosition))
        }
    }

    private fun updateCurrentBuffer(exo: ExoPlayer) {
        _currentBuffer.value = exo.totalBufferedDuration.toInt()
    }

    private fun observeCMCDProperties() {
        viewModelScope.launch {
            _currentBuffer.collect {
                cmcdAppManager.updateBufferLength(it)
                cmcdAppManager.updateDeadline(it)
            }
            _currentBitrate.collect {
                cmcdAppManager.updateEncodedBitrate(it)
            }
            _measuredThroughput.collect {
                cmcdAppManager.updateMeasuredThroughput(it)
            }
            _streamType.collect { st ->
                st?.let { cmcdAppManager.updateStreamType(it) }
            }
            _playbackRate.collect {
                cmcdAppManager.updatePlaybackRate(it)
            }
        }
    }
}
