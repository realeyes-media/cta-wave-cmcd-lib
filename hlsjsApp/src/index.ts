import { Media } from "./models/Media";
import { Player } from "./player";

const videoEl = document.getElementById("video") as HTMLVideoElement;
const cmcdDebugEl = document.getElementById("cmcdDebug") as HTMLElement;

const player = new Player(videoEl, cmcdDebugEl);

const media: Media = {
    id: "rjsfzpcmsipb8nzusz",
    uri: "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
    title: "Big Buck Bunny",
    format: "hls",
};

player.initPlayer(media);
