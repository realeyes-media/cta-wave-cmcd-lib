package tech.ctawave.exoplayercmcd.util

import android.annotation.SuppressLint
import java.lang.String.format
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Status of a resource that is provided to the UI.
 */
@Singleton
class TimeUtils @Inject constructor() {

    @SuppressLint("DefaultLocale")
    fun convertMilliSecondsToTimeFormat(millis: Long): String {
        return format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}
