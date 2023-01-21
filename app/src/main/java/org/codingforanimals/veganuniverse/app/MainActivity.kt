package org.codingforanimals.veganuniverse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.codingforanimals.veganuniverse.ui.VeganUniverseApp
import org.codingforanimals.veganuniverse.ui.theme.VeganUniverseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeganUniverseTheme {
                VeganUniverseApp()
            }
        }
    }
}
