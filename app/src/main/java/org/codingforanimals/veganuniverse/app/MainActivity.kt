package org.codingforanimals.veganuniverse.app

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.VUApp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val fineLocation = permissions[ACCESS_FINE_LOCATION]
                mainViewModel.onPermissionsChange(fineLocation)
            }

            locationPermissionRequest.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
            )
        }
        setContent {
            VeganUniverseTheme {
                val navController = rememberNavController()
                VUApp(
                    navController = navController
                )

                val onboardingDismissed by mainViewModel.wasOnboardingDismissed.collectAsStateWithLifecycle()
                AnimatedVisibility(
                    visible = !onboardingDismissed,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    OnboardingScreen(onDismiss = { mainViewModel.setOnboardingAsDismissed() })
                }

                LaunchedEffect(Unit) {
                    mainViewModel.deeplinkFlow.collectLatest { deeplink ->
                        navController.navigate(deeplink)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.evaluateUserVerification()
    }
}
