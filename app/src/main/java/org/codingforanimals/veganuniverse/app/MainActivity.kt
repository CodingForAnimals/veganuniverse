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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.navigation.model.DeepLinkNavigationOptions
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.navigation.VeganUniverseApp
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
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
                VeganUniverseBackground(
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    val navController = rememberNavController()
                    VeganUniverseApp(
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
                        mainViewModel.deeplinkFlow.collectLatest { deeplinkNavigation ->
                            navController.navigate(
                                deepLink = deeplinkNavigation.uri,
                                navOptions = deeplinkNavigation.options?.toNavOptions(),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun DeepLinkNavigationOptions.toNavOptions(): NavOptions {
        var options = NavOptions.Builder()
        popUpTo?.let { options = options.setPopUpTo(it, inclusive) }
        return options.build()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.evaluateUserVerification()
    }
}
