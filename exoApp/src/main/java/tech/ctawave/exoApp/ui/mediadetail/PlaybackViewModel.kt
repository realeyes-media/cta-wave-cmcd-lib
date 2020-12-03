package tech.ctawave.exoApp.ui.mediadetail

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import kotlinx.coroutines.flow.MutableStateFlow
import tech.ctawave.exoApp.CMCDAppManager
import tech.ctawave.exoApp.R
import tech.ctawave.exoApp.data.entities.StreamingFormat
import tech.ctawave.exoApp.util.templateFormat

class PlaybackViewModel @ViewModelInject constructor(application: Application, private val cmcdAppManager: CMCDAppManager) :
    AndroidViewModel(application),
    Player.EventListener, AnalyticsListener {

    private val applicationContext = application.applicationContext
    private var exoPlayer: SimpleExoPlayer? = null

    // CMCD
    var contentId: String? = null
    var streamingFormat: StreamingFormat? = null

    val currentBitrate = MutableStateFlow(templateFormat(R.string.current_bitrate, applicationContext, 0))
    val currentFramerate = MutableStateFlow(templateFormat(R.string.current_framerate, applicationContext, 0))
    val currentPlayhead = MutableStateFlow(templateFormat(R.string.current_playhead, applicationContext, 0))
    val currentPlaylistUri = MutableStateFlow(templateFormat(R.string.current_playlist_uri, applicationContext, 0))

    fun setPlayer(exoPlayer: SimpleExoPlayer) {
        this.exoPlayer = exoPlayer
    }

    fun initCMCD() {
        if (contentId != null && streamingFormat != null) {
            cmcdAppManager.initForContentId(contentId!!, streamingFormat!!)
        }
    }

    fun poll() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object: Runnable {
            override fun run() {
                handler.postDelayed(this, 100)
                val position = exoPlayer?.currentPosition ?: 0L
                println("$$$ position: $position")
                currentPlayhead.value = templateFormat(R.string.current_playhead, applicationContext, position)
            }
        }, 100)
    }

    override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime, surface: Surface?) {
        super.onRenderedFirstFrame(eventTime, surface)
        exoPlayer?.let {
            val bitrateValue = it.videoFormat?.bitrate ?: 0
            currentBitrate.value = templateFormat(R.string.current_bitrate, applicationContext, bitrateValue)

            val frameRateValue = it.videoFormat?.frameRate ?: 0f
            currentFramerate.value = templateFormat(R.string.current_framerate, applicationContext, frameRateValue)
        }
    }

    override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime, reason: Int) {
        super<AnalyticsListener>.onTimelineChanged(eventTime, reason)
        exoPlayer?.currentMediaItem?.playbackProperties?.let {
            val uri = it.uri.toString()
            currentPlaylistUri.value = templateFormat(R.string.current_playlist_uri, applicationContext, uri)
        }
    }
}
