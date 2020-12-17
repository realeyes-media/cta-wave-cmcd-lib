import cmcdlib from "cmcdlib-cmcd";
import { ObjectType } from "./models/ObjectType";
import { StreamingFormat } from "./models/StreamingFormat";
import { StreamType } from "./models/StreamType";

export class CMCDAppManager {
    private manager: cmcdlib.tech.ctawave.cmcd.CMCDManager;

    constructor(contentId: string, streamingFormat: StreamingFormat) {
        const config = new cmcdlib.tech.ctawave.cmcd.CMCDConfig(contentId, streamingFormat);
        console.log("$$$ cmcd > init");
        this.manager = cmcdlib.tech.ctawave.cmcd.CMCDManagerFactory.createCMCDManager(config);
    }

    createCMCDCompliantUri(uri: string, objectType: ObjectType, startup = false): string {
        return this.manager.appendQueryParamsToUri(uri, objectType, startup);
    }

    updateBufferLength(bufferLength: number): void {
        console.log(`$$$ cmcd > updateBufferLength: ${bufferLength}`);
        this.manager.bufferLength = bufferLength;
    }

    updateEncodedBitrate(encodedBitrate: number): void {
        console.log(`$$$ cmcd > updateEncodedBitrate: ${encodedBitrate}`);
        this.manager.encodedBitrate = encodedBitrate;
    }

    updateBufferStarvation(bufferStarvation: boolean): void {
        console.log(`$$$ cmcd > updateBufferStarvation: ${bufferStarvation}`);
        this.manager.bufferStarvation = bufferStarvation;
    }

    updateObjectDuration(objectDuration: number): void {
        console.log(`$$$ cmcd > objectDuration: ${objectDuration}`);
        this.manager.objectDuration = objectDuration;
    }

    updateDeadline(deadline: number): void {
        console.log(`$$$ cmcd > updateDeadline: ${deadline}`);
        this.manager.deadline = deadline;
    }

    updateMeasuredThroughput(measuredThroughput: number): void {
        console.log(`$$ cmcd > updateMeasuredThroughput: ${measuredThroughput}`);
        this.manager.measuredThroughput = measuredThroughput;
    }

    updateNextObjectRequest(nextObjectRequest: string): void {
        console.log("$$$ cmcd > updateNextObjectRequest: $nextObjectRequest");
        this.manager.nextObjectRequest = nextObjectRequest;
    }

    updateNextRangeRequest(nextRangeRequest: string): void {
        console.log(`$$$ cmcd > updateNextRangeRequest: ${nextRangeRequest}`);
        this.manager.nextRangeRequest = nextRangeRequest;
    }

    updatePlaybackRate(playbackRate: number): void {
        console.log(`$$$ cmcd > updatePlaybackRate: ${playbackRate}`);
        this.manager.playbackRate = playbackRate;
    }

    updateRequestedMaximumThroughput(requestedMaximumThroughput: number): void {
        console.log(`$$$ cmcd > updateRequestedMaximumThroughput: ${requestedMaximumThroughput}`);
        this.manager.requestedMaximumThroughput = requestedMaximumThroughput;
    }

    updateStreamType(streamType: StreamType): void {
        console.log("$$$ cmcd > updateStreamType: $cmcdStreamType");
        this.manager.streamType = streamType;
    }

    updateTopBitrate(topBitrate: number): void {
        console.log(`$$$ cmcd > updateTypBitrate: ${topBitrate}`);
        this.manager.topBitrate = topBitrate;
    }
}
