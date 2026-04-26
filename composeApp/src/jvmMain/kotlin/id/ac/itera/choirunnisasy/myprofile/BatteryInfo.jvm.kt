package id.ac.itera.choirunnisasy.myprofile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class BatteryInfo actual constructor() {
    actual fun getBatteryLevel(): Int = 100 // Default for desktop
    actual fun isCharging(): Boolean = true
}
