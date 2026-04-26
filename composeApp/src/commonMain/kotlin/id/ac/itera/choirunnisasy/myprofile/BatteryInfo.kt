package id.ac.itera.choirunnisasy.myprofile

expect class BatteryInfo {
    fun getBatteryLevel(): Int
    fun isCharging(): Boolean
}
