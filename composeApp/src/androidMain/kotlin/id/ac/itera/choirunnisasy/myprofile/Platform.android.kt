package id.ac.itera.choirunnisasy.myprofile

import android.os.Build
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import android.content.Context
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Note: This helper might need a Context. In a real app, you'd pass it from MainActivity
// or use a DI framework. For now, we'll use a placeholder or assume it's called where context is available.
// However, since createSettings() is expect/actual, it's often better to have it as a class or 
// use a library like Koin to inject it.
// To keep it simple for now, we'll define a factory that can be used.

private var appContext: Context? = null

fun initAndroidSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun createSettings(): ObservableSettings {
    val context = appContext ?: throw IllegalStateException("Context not initialized. Call initAndroidSettings first.")
    val delegate = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(delegate)
}
