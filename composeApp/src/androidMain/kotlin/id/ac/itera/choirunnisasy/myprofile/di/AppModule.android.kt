package id.ac.itera.choirunnisasy.myprofile.di

import dev.icerock.moko.permissions.PermissionsController
import id.ac.itera.choirunnisasy.myprofile.NetworkMonitor
import id.ac.itera.choirunnisasy.myprofile.BatteryInfo
import org.koin.dsl.module

actual val platformModule = module {
    single { PermissionsController(applicationContext = get()) }
    single { NetworkMonitor(context = get()) }
    single { BatteryInfo(context = get()) }
}
