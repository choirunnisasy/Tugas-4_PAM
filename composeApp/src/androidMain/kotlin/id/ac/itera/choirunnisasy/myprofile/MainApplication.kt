package id.ac.itera.choirunnisasy.myprofile

import android.app.Application
import id.ac.itera.choirunnisasy.myprofile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
