package id.ac.itera.choirunnisasy.myprofile.di

import id.ac.itera.choirunnisasy.myprofile.data.NoteRepository
import id.ac.itera.choirunnisasy.myprofile.data.NoteRepositoryImpl
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.db.NotesDatabase
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ProfileViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.SettingsViewModel
import id.ac.itera.choirunnisasy.myprofile.ui.NewsViewModel
import id.ac.itera.choirunnisasy.myprofile.PlatformSpecificFetcher
import id.ac.itera.choirunnisasy.myprofile.data.AIRepository
import id.ac.itera.choirunnisasy.myprofile.data.AIRepositoryImpl
import id.ac.itera.choirunnisasy.myprofile.data.GeminiService
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ChatViewModel
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val dataModule = module {
    single { SettingsManager(get()) }
    
    single<NotesDatabase> { 
        val driverFactory: DatabaseDriverFactory = get()
        NotesDatabase(driverFactory.createDriver()) 
    }
    
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    singleOf(::PlatformSpecificFetcher)

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = false 
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP: $message")
                    }
                }
            }
        }
    }
    
    single { GeminiService(get()) }
    single<AIRepository> { AIRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::NoteViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::NewsViewModel)
    viewModelOf(::ChatViewModel)
}

val appModule = module {
    includes(platformModule, dataModule, viewModelModule)
}
