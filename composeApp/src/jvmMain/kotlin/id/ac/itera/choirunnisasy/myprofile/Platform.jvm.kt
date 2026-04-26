package id.ac.itera.choirunnisasy.myprofile

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun createSettings(): ObservableSettings = PreferencesSettings(Preferences.userRoot().node("id.ac.itera.choirunnisasy.myprofile"))
