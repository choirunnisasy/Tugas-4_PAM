package id.ac.itera.choirunnisasy.myprofile

import android.os.Build
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import android.content.Context

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// Fungsi pembantu untuk Koin di Android
fun createAndroidSettings(context: Context): ObservableSettings {
    val delegate = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(delegate)
}

// Actual ini tetap ada untuk memenuhi kontrak, tapi kita gunakan DI untuk Android
actual fun createSettings(): ObservableSettings {
    throw UnsupportedOperationException("Gunakan createAndroidSettings(context) untuk platform Android")
}
