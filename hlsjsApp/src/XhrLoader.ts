import Hls from "hls.js";
import { CMCDAppManager } from "./CMCDAppManager";
import { ObjectType } from "./models/ObjectType";

export class XhrLoader extends Hls.DefaultConfig.loader {
    static cmcdManager?: CMCDAppManager;
    static debugEl?: HTMLElement;

    constructor(config: Hls.LoaderConfig) {
        super(config);
        const load = this.load.bind(this);
        this.load = (context, config, callbacks) => {
            const onSuccess = callbacks.onSuccess;
            callbacks.onSuccess = (response, stats, context) => {
                XhrLoader.cmcdManager?.updateMeasuredThroughput(stats.bw);
                onSuccess(response, stats, context);
            };

            const ot = this.determineObjectType(context);
            context.url = XhrLoader.cmcdManager?.createCMCDCompliantUri(context.url, ot, false) || context.url;
            console.log("$$$ XhrLoader > cmcdUrl: ", context.url);

            const debugEl = XhrLoader.debugEl;
            if (debugEl != null) {
                debugEl.innerHTML = context.url;
            }

            load(context, config, callbacks);
        };
    }

    private determineObjectType(context: Hls.LoaderContext): ObjectType {
        let ot: ObjectType;

        if (context.frag != null) {
            XhrLoader.cmcdManager?.updateObjectDuration(context.frag.duration);
            ot = ObjectType.MUXED_AUDIO_VIDEO;
        } else if (context.type === "level" || context.type === "manifest") {
            ot = ObjectType.MANIFEST;
        } else if (context.url.includes(".vtt")) {
            ot = ObjectType.CAPTION_OR_SUBTITLE;
        } else {
            ot = ObjectType.OTHER;
        }

        return ot;
    }
}
