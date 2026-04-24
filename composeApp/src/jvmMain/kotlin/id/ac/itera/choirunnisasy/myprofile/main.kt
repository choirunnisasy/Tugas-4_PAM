package id.ac.itera.choirunnisasy.myprofile

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MyProfileApp",
    ) {
        App(DatabaseDriverFactory())
    }
}
