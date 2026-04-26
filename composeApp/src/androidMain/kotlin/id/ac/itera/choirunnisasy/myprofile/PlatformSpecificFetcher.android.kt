package id.ac.itera.choirunnisasy.myprofile

import android.os.Build

actual class PlatformSpecificFetcher actual constructor() {
    actual fun getOsName(): String = "Android ${Build.VERSION.RELEASE}"
    actual fun getAppVersion(): String = "1.0.0"
}
