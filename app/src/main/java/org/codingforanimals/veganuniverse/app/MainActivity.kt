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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.VeganUniverseApp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
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

                        VeganUniverseApp()

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