package tech.ctawave.exoplayercmcd.ui.mediadetail

import android.app.Application
import android.view.Surface
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.ctawave.exoplayercmcd.R
import tech.ctawave.exoplayercmcd.util.TimeUtils
import java.lang.Exception
import java.text.MessageFormat

class PlaybackViewModel @ViewModelInject constructor(application: Application, private val timeUtils: TimeUtils) :
    AndroidViewModel(application),
    Player.EventListener, AnalyticsListener {

    private val applicationContext = application.applicationContext
    private var exoPlayer: SimpleExoPlayer? = null
    var coroutineScope: CoroutineScope = viewModelScope
    var bitrateLiveData: MutableLiveData<String> = MutableLiveData(formatMessage(R.string.current_bitrate, 0))
    var frameRateLiveData: MutableLiveData<String> = MutableLiveData(formatMessage(R.string.current_framerate, 0))
    var currentTimeLiveData: MutableLiveData<String> = MutableLiveData(formatMessage(R.string.current_position, 0))
    var playListUriLiveData: MutableLiveData<String> = MutableLiveData(formatMessage(R.string.current_playlist_uri, 0))

    fun setPlayer(exoPlayer: SimpleExoPlayer) {
        this.exoPlayer = exoPlayer
    }

    fun poll() {
        var position = 0L
        coroutineScope.launch {
            withContext(Dispatchers.Default)
            {
                try {
                    while (true) {
                        withContext(Dispatchers.Main)
                        {
                            position = exoPlayer?.currentPosition ?: 0L
                        }
                        currentTimeLiveData.postValue(
                            formatMessage(
                                R.string.current_position,
                                timeUtils.convertMilliSecondsToTimeFormat(position)
                            )
                        )
                        delay(200)
                    }
                } catch (exception: Exception) {
                }
            }

        }
    }

    private fun formatMessage(stringId: Int, vararg args: Any?): String {
        val fmt = applicationContext.getString(stringId)
        return MessageFormat(fmt).format(args)
    }

    override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime, surface: Surface?) {
        super.onRenderedFirstFrame(eventTime, surface)
        exoPlayer?.let {
            val bitrateValue = it.videoFormat?.bitrate ?: 0
            bitrateLiveData.value = formatMessage(R.string.current_bitrate, bitrateValue*0.000125)

            val frameRateValue = it.videoFormat?.frameRate ?: 0f
            frameRateLiveData.value = formatMessage(R.string.current_framerate, frameRateValue)
        }
    }

    override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime, reason: Int) {
        super<AnalyticsListener>.onTimelineChanged(eventTime, reason)
        playListUriLiveData.value = formatMessage(
            R.string.current_playlist_uri,
            exoPlayer?.currentMediaItem?.playbackProperties?.uri?.toString()
        )
    }
}
