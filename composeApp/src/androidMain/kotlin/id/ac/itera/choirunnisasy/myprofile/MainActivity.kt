package id.ac.itera.choirunnisasy.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Inisialisasi settings untuk Android
        initAndroidSettings(this)

        setContent {
            App(DatabaseDriverFactory(this))
        }
    }
}
