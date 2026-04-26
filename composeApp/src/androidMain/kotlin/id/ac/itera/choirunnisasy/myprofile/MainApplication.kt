package id.ac.itera.choirunnisasy.myprofile

import android.app.Application
import id.ac.itera.choirunnisasy.myprofile.di.appModule
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                appModule,
                module {
                    single { DatabaseDriverFactory(get()) }
                }
            )
        }
    }
}
