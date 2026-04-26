package id.ac.itera.choirunnisasy.myprofile.di

import id.ac.itera.choirunnisasy.myprofile.createSettings
import id.ac.itera.choirunnisasy.myprofile.data.NoteRepository
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.db.NotesDatabase
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ProfileViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.SettingsViewModel
import id.ac.itera.choirunnisasy.myprofile.ui.NewsViewModel
import id.ac.itera.choirunnisasy.myprofile.PlatformSpecificFetcher
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    includes(platformModule)

    single { createSettings() }
    singleOf(::SettingsManager)
    
    single<NotesDatabase> { 
        val driverFactory: DatabaseDriverFactory = get()
        NotesDatabase(driverFactory.createDriver()) 
    }
    
    singleOf(::NoteRepository)
    singleOf(::PlatformSpecificFetcher)
    
    viewModelOf(::NoteViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::NewsViewModel)
}
