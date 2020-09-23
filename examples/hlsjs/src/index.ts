import { CMCDManagerFactory } from "cmcd";

export const cmcdManager = CMCDManagerFactory.createCMCDManager();

console.log(cmcdManager.hello);
