actual object CMCDManagerFactory {
    actual fun  createCMCDManager(): CMCDManager = CMCDManagerAndroid
}

object CMCDManagerAndroid: CMCDManager {
    override val hello = "hello!"
}
