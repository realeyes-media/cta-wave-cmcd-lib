import { CMCDConfig, CMCDManagerFactory, CMCDStreamingFormat } from "cmcdlib-cmcd";

const config = new CMCDConfig("test-content-id");
export const cmcdManager = CMCDManagerFactory.createCMCDManager(config);

console.log(cmcdManager.encodedBitrate);
