declare module "cmcdlib-cmcd" {
    declare namespace tech.ctawave.cmcd {
        class CMCDManager {
            bufferLength: ?number;
            encodedBitrate: ?number;
            bufferStarvation: boolean;
            contentId: string;
            objectDuration: ?number;
            deadline: ?number;
            measuredThroughput: ?number;
            nextObjectRequest: ?string;
            nextRangeRequest: ?string;
            objectType: ?string;
            playbackRate: ?number;
            requestedMaximumThroughput: ?number;
            sessionId: string;
            streamingFormat: string;
            startup: boolean;
            streamType: ?string;
            topBitrate: ?number;
            version: number;

            appendQueryParamsToUri(uri: string, objectType: string, startup: boolean = false): string;
            validate(queryParams: string): boolean;
        }

        class CMCDManagerFactory {
            static createCMCDManager(config: CMCDConfig): CMCDManager;
        }

        class CMCDConfig {
            contentId: string;
            streamingFormat: string;
            sessionId: string;
            version: number;

            constructor(contentId: string, streamingForamt: string, sessionId: string = "", version = 1);
        }
    }
}
