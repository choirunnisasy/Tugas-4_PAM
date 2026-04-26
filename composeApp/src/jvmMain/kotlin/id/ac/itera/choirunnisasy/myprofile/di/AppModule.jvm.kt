package id.ac.itera.choirunnisasy.myprofile.di

import id.ac.itera.choirunnisasy.myprofile.NetworkMonitor
import org.koin.dsl.module

actual val platformModule = module {
    single { NetworkMonitor() }
}
