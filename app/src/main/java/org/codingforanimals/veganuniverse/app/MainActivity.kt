package org.codingforanimals.veganuniverse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.VUApp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val db = FirebaseFirestore.getInstance()
        Firebase.firestore.useEmulator("10.0.2.2", 8080)
        db.collection("asd").add(mapOf("holaj" to "que tal"))
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var launchState: MainViewModel.LaunchState by mutableStateOf(MainViewModel.LaunchState.Loading)
//        window.statusBarColor = Color.Transparent.toArgb()
//        val wic = WindowCompat.getInsetsController(window, window.decorView)
//        wic.isAppearanceLightStatusBars = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.onEach {
                    launchState = it
                }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (launchState) {
                MainViewModel.LaunchState.Loading -> true
                is MainViewModel.LaunchState.Completed -> false
            }
        }
//
        setContent {
            VeganUniverseTheme {
                when (val state = launchState) {
                    MainViewModel.LaunchState.Loading -> Unit
                    is MainViewModel.LaunchState.Completed -> {

                        VUApp()

                        var showOnboarding by remember { mutableStateOf(state.showOnboarding) }
                        AnimatedVisibility(
                            visible = showOnboarding,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            OnboardingScreen(onDismiss = { showOnboarding = false })
                        }
                    }
                }
            }
        }
    }
}