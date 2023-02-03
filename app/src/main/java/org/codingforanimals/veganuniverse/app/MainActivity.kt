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
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.VeganUniverseApp
import org.codingforanimals.veganuniverse.ui.theme.VeganUniverseTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val splashViewModel: SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var splashUiState: SplashViewModel.SplashUiState by mutableStateOf(SplashViewModel.SplashUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.uiState.onEach {
                    splashUiState = it
                }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (splashUiState) {
                SplashViewModel.SplashUiState.Loading -> true
                is SplashViewModel.SplashUiState.Completed -> false
            }
        }

        setContent {
            VeganUniverseTheme {
                when (val splashState = splashUiState) {
                    SplashViewModel.SplashUiState.Loading -> Unit
                    is SplashViewModel.SplashUiState.Completed -> {
                        VeganUniverseApp()

                        var showOnboarding by remember { mutableStateOf(splashState.showOnboarding) }
                        AnimatedVisibility(
                            visible = showOnboarding,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            OnboardingScreen(
                                onDismiss = {
                                    splashViewModel.onboardingScreenDismissed()
                                    showOnboarding = false
                                })
                        }
                    }
                }
            }
        }
    }
}