class CMCDManagerCommonFactory {
    fun createCMCDManager(versionLibrary: VersionLibrary): CMCDManagerCommon {
        val versionObj: Version = when (versionLibrary) {
            VersionLibrary.VERSION_1 -> Version1(
                mutableMapOf(
                    Version1Keys.br.name to CMCDProperty(CMCDHeaderName.Object, "", 256),
                    Version1Keys.bl.name to CMCDProperty(CMCDHeaderName.Object, "", 5),
                    Version1Keys.d.name to CMCDProperty(CMCDHeaderName.Object, "", 10),
                    Version1Keys.sf.name to CMCDProperty(CMCDHeaderName.Object, "", CMCDStreamingFormat.d),

                    )
            )
        }

        return CMCDManagerCommon(versionObj)
    }
}
