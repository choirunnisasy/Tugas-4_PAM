package id.ac.itera.choirunnisasy.myprofile.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, platformModule)
    }

// For iOS
fun initKoin() = initKoin {}
