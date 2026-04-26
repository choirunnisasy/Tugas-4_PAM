package id.ac.itera.choirunnisasy.myprofile

actual class PlatformSpecificFetcher actual constructor() {
    actual fun getOsName(): String = "Desktop"
    actual fun getAppVersion(): String = "1.0.0"
}
