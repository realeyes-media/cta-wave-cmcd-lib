declare module "cmcd" {
    class CMCDManager {
        hello: string;
    }
    class CMCDManagerFactory {
        static createCMCDManager(): CMCDManager;
    }
}
