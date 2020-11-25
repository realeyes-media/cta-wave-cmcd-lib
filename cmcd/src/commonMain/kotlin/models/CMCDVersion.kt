package models

/**
 * CMCD Version Number
 */
enum class CMCDVersion(val number: Int) {
    VERSION_1(1);

    companion object {
        fun from(str: String?): CMCDVersion? {
            return when(str) {
                "1" -> VERSION_1
                else -> null
            }
        }
    }
}
