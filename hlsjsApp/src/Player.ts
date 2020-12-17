import { Media } from "./models/Media";
import Hls from "hls.js";
import { CMCDAppManager } from "./CMCDAppManager";
import { StreamingFormat } from "./models/StreamingFormat";
import { XhrLoader } from "./XhrLoader";

export class Player {
    private hls: Hls | undefined;
    private media?: Media;
    private cmcdManager?: CMCDAppManager;

    constructor(private videoEl: HTMLVideoElement, private debugEl: HTMLElement) {}

    initPlayer(media: Media): void {
        this.media = media;

        if (this.hls != undefined) {
            this.releasePlayer();
        }

        if (Hls.isSupported()) {
            this.initCMCDManager(media);

            XhrLoader.cmcdManager = this.cmcdManager;
            XhrLoader.debugEl = this.debugEl;

            this.hls = new Hls({ loader: XhrLoader });

            this.hls?.attachMedia(this.videoEl);

            this.addHlsListeners();
            this.addVideoElListeners();
        }
    }

    releasePlayer(): void {
        this.hls?.detachMedia();
        this.hls?.destroy();
        this.hls = undefined;
        XhrLoader.debugEl = undefined;
        XhrLoader.cmcdManager = undefined;
    }

    private onMediaAttached(): void {
        if (this.media == undefined) {
            return;
        }
        this.hls?.loadSource(this.media.uri);
    }

    private onLevelUpdated(data: Hls.levelUpdatedData): void {
        const lvl = this.hls?.levels[data.level];
        if (lvl != null) {
            const bitrate = lvl.bitrate;
            this.cmcdManager?.updateEncodedBitrate(bitrate);
        }
    }

    private onFragBuffered(): void {
        const playhead = this.videoEl.currentTime;
        const bufferedTimeRanges = this.videoEl.buffered;

        for (let i = 0; i < bufferedTimeRanges.length, i++; ) {
            const start = bufferedTimeRanges.start(i);
            const end = bufferedTimeRanges.end(i);
            if (playhead >= start && playhead < end) {
                const totalBuffer = end - playhead;
                this.cmcdManager?.updateBufferLength(totalBuffer);
                this.cmcdManager?.updateDeadline(totalBuffer);
                return;
            }
        }
    }

    private addHlsListeners() {
        this.hls?.on(Hls.Events.MEDIA_ATTACHED, () => this.onMediaAttached());
        this.hls?.on(Hls.Events.LEVEL_UPDATED, (_, data) => this.onLevelUpdated(data));
        this.hls?.on(Hls.Events.FRAG_BUFFERED, () => this.onFragBuffered());
    }

    private onPlaybackRateChanged() {
        this.cmcdManager?.updatePlaybackRate(this.videoEl.playbackRate);
    }

    private addVideoElListeners() {
        this.videoEl.addEventListener("ratechange", () => this.onPlaybackRateChanged());
    }

    private initCMCDManager(media: Media) {
        let format: StreamingFormat;
        switch (media.format) {
            case "hls":
                format = StreamingFormat.HLS;
            case "dash":
                format = StreamingFormat.MPEG_DASH;
            case "smooth":
                format = StreamingFormat.SMOOTH_STREAMING;
            default:
                format = StreamingFormat.HLS;
        }
        this.cmcdManager = new CMCDAppManager(media.id, format);
    }
}
