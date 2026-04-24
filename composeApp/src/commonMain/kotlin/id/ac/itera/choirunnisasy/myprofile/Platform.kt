package id.ac.itera.choirunnisasy.myprofile

import com.russhwolf.settings.ObservableSettings

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun createSettings(): ObservableSettings