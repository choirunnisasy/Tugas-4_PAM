package id.ac.itera.choirunnisasy.myprofile.di

import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.ios.PermissionsController as IosPermissionsController
import id.ac.itera.choirunnisasy.myprofile.NetworkMonitor
import id.ac.itera.choirunnisasy.myprofile.BatteryInfo
import org.koin.dsl.module

actual val platformModule = module {
    single<PermissionsController> { IosPermissionsController() }
    single { NetworkMonitor() }
    single { BatteryInfo() }
}
