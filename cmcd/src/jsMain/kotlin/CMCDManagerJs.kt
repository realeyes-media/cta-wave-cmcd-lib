actual object CMCDManagerFactory {
    actual fun createCMCDManager(): CMCDManager = CMCDManagerJs
}

object CMCDManagerJs: CMCDManager {
    override val hello = "hello!"
}
