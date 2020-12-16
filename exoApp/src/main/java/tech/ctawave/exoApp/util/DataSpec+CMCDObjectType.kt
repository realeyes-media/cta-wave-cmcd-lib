package tech.ctawave.exoApp.util

import com.google.android.exoplayer2.upstream.DataSpec
import tech.ctawave.cmcd.models.CMCDObjectType

/**
 * CMCDObjectTypes:
 *
 * Manifest
 * Segment: AudioOnly, VideoOnly, MuxedAudioVideo
 * Init Segment
 * Key
 * Timed Text Track
 * Caption or Subtitle
 * Other
 */
fun DataSpec.determineCMCDObjectType(): CMCDObjectType {
    val uri = this.uri

    fun isMp4(str: String): Boolean {
        return str.contains(".mp4") || str.contains(".m4s") || str.contains(".fmp4")
    }

    uri.lastPathSegment?.let {
        // these are all just an examples, production would require a better approach

        if (it.contains(".m3u8")) {
            return CMCDObjectType.MANIFEST
        }

        if (it.contains(".vtt")) {
            return CMCDObjectType.CAPTION_OR_SUBTITLE
        }

        if (it.contains(".ts") || isMp4(it)) {
            return CMCDObjectType.MUXED_AUDIO_VIDEO
        }
    }

    return CMCDObjectType.OTHER
}
