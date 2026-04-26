package id.ac.itera.choirunnisasy.myprofile

expect class PlatformSpecificFetcher() {
    fun getOsName(): String
    fun getAppVersion(): String
}
