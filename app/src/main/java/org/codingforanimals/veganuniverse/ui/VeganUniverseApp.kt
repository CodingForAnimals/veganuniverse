package org.codingforanimals.veganuniverse.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.onboarding.presentation.SetOnboardingAsDismissedUseCase
import org.codingforanimals.veganuniverse.onboarding.presentation.ShowOnboardingUseCase
import org.codingforanimals.veganuniverse.ui.theme.VeganUniverseTheme
import org.koin.java.KoinJavaComponent.get

@Composable
internal fun VeganUniverseApp(
    appState: VeganUniverseAppState = rememberVeganUniverseAppState(),
    showOnboardingUseCase: ShowOnboardingUseCase = get(ShowOnboardingUseCase::class.java),
    setOnboardingAsDismissedUseCase: SetOnboardingAsDismissedUseCase = get(
        SetOnboardingAsDismissedUseCase::class.java
    ),
) {
    var showOnboarding by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appState.coroutineScope.launch { showOnboarding = showOnboardingUseCase() }
    }

    VeganUniverseBackground {
        AppContent(appState = appState, showNavBar = !showOnboarding)

        OnboardingScreen(
            showOnboarding = showOnboarding,
            onDismiss = {
                appState.coroutineScope.launch { setOnboardingAsDismissedUseCase() }
                showOnboarding = false
            },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val appState = rememberVeganUniverseAppState()
    VeganUniverseTheme {
        VeganUniverseAppNavHost(
            navController = appState.navController,
            snackbarHostState = SnackbarHostState(),
            cameraPositionState = rememberCameraPositionState()
        )
    }
}

