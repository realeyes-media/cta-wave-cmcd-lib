interface CMCDManager {
    val hello: String
}

expect object CMCDManagerFactory {
    fun createCMCDManager(): CMCDManager
}
