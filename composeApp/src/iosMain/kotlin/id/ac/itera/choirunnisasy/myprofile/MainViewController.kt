package id.ac.itera.choirunnisasy.myprofile

import androidx.compose.ui.window.ComposeUIViewController
import id.ac.itera.choirunnisasy.myprofile.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { 
    App() 
}
