package id.ac.itera.choirunnisasy.myprofile

import platform.UIKit.UIDevice

actual class PlatformSpecificFetcher actual constructor() {
    actual fun getOsName(): String = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"
    actual fun getAppVersion(): String = "1.0.0"
}
