package id.ac.itera.choirunnisasy.myprofile

import androidx.compose.ui.window.ComposeUIViewController
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { 
    App(DatabaseDriverFactory()) 
}