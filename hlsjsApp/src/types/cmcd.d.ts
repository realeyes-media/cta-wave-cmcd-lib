declare module "cmcdlib-cmcd" {
    class CMCDManager {
        bufferLength: CMCDPayload.BufferLength;
        encodedBitrate: CMCDPayload.EncodedBitrate;
        bufferStarvation: CMCDPayload.BufferStarvation;
        contentId: CMCDPayload.ContentId;
        objectDuration: CMCDPayload.ObjectDuration;
        deadline: CMCDPayload.Deadline;
        measuredThroughput: CMCDPayload.MeasuredThroughput;
        nextObjectRequest: CMCDPayload.NextObjectRequest;
        nextRangeRequest: CMCDPayload.NextRangeRequest;
        objectType: CMCDPayload.ObjectType;
        playbackRate: CMCDPayload.PlaybackRate;
        requestedMaximumThroughput: CMCDPayload.RequestedMaximumThroughput;
        sessionId: CMCDPayload.SessionId;
        streamingFormat: CMCDPayload.StreamingFormat;
        startup: CMCDPayload.Startup;
        streamType: CMCDPayload.StreamType;
        topBitrate: CMCDPayload.TopBitrate;
        version: CMCDPayload.Version;

        debug: boolean;

        appendQueryParams(url: string, objectType: CMCDObjectType, startup: boolean = false): string;
        validate(queryParams: string): boolean;
    }

    class CMCDManagerFactory {
        static createCMCDManager(config: CMCDConfig): CMCDManager;
    }

    class CMCDConfig {
        contentId: string;
        streamingFormat: CMCDStreamingFormat;
        sessionId: string;
        debug: boolean;
        version: CMCDVersion;

        constructor(
            contentId: string,
            streamingForamt: CMCDStreamingFormat,
            sessionId: string = "",
            debug: boolean = false,
            version: CMCDVersion = CMCDVersion.VERSION_1
        );
    }

    enum CMCDStreamingFormat {
        MPEG_DASH = "d",
        HLS = "h",
        SMOOTH_STREAMING = "s",
        OTHER = "o",
    }

    enum CMCDObjectType {
        MANIFEST = "m",
        AUDIO_ONLY = "a",
        VIDEO_ONLY = "v",
        MUXED_AUDIO_VIDEO = "av",
        INIT_SEGMENT = "i",
        CAPTION_OR_SUBTITLE = "c",
        TIMED_TEXT_TRACK = "tt",
        KEY = "k",
        OTHER = "o",
    }

    enum CMCDKey {
        BUFFER_LENGTH,
        ENCODED_BITRATE,
        BUFFER_STARVATION,
        CONTENT_ID,
        OBJECT_DURATION,
        OBJECT_TYPE,
        DEADLINE,
        MEASURED_THROUGHPUT,
        NEXT_OBJECT_REQUEST,
        NEXT_RANGE_REQUEST,
        PLAYBACK_RATE,
        REQUESTED_MAXIMUM_THROUGHPUT,
        SESSION_ID,
        STREAMING_FORMAT,
        STREAM_TYPE,
        STARTUP,
        TOP_BITRATE,
        VERSION,
    }

    enum CMCDHeader {
        OBJECT = "CMCD-Object",
        REQUEST = "CMCD-Request",
        SESSION = "CMCD-Session",
        STATUS = "CMCD-Status",
    }

    enum CMCDVersion {
        VERSION_1 = 1,
    }

    enum CMCDStreamType {
        VOD = "v",
        LIVE = "l",
    }

    namespace CMCDPayload {
        class BufferLength {
            value: ?number;
            key = CMCDKey.BUFFER_LENGTH;
        }
        class EncodedBitrate {
            value: ?number;
            key = CMCDKey.ENCODED_BITRATE;
        }
        class BufferStarvation {
            value: boolean;
            key = CMCDKey.BUFFER_STARVATION;
        }
        class ContentId {
            value: string;
            key = CMCDKey.CONTENT_ID;
        }
        class ObjectDuration {
            value: ?number;
            key = CMCDKey.OBJECT_DURATION;
        }
        class ObjectType {
            value: CMCDObjectType;
            key = CMCDKey.OBJECT_TYPE;
        }
        class Deadline {
            value: ?number;
            key = CMCDKey.DEADLINE;
        }
        class MeasuredThroughput {
            value: ?number;
            key = CMCDKey.MEASURED_THROUGHPUT;
        }
        class NextObjectRequest {
            value: ?string;
            key = CMCDKey.NEXT_OBJECT_REQUEST;
        }
        class NextRangeRequest {
            value: ?string;
            key = CMCDKey.NEXT_RANGE_REQUEST;
        }
        class PlaybackRate {
            value: ?number;
            key = CMCDKey.PLAYBACK_RATE;
        }
        class RequestedMaximumThroughput {
            value: ?number;
            key = CMCDKey.REQUESTED_MAXIMUM_THROUGHPUT;
        }
        class SessionId {
            value: string;
            key = CMCDKey.SESSION_ID;
        }
        class StreamingFormat {
            value: CMCDStreamingFormat;
            key = CMCDKey.STREAMING_FORMAT;
        }
        class StreamType {
            value: ?CMCDStreamType;
            key = CMCDKey.STREAM_TYPE;
        }
        class Startup {
            value: boolean;
            key = CMCDKey.STARTUP;
        }
        class TopBitrate {
            value: ?number;
            key = CMCDKey.TOP_BITRATE;
        }
        class Version {
            value: CMCDVersion;
            key = CMCDKey.VERSION;
        }
    }
}
