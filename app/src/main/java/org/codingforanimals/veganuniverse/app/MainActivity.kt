package org.codingforanimals.veganuniverse.app

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.VUApp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

//        FirebaseConfig.useEmulators()

//        window.statusBarColor = Color.Transparent.toArgb()
//        val wic = WindowCompat.getInsetsController(window, window.decorView)
//        wic.isAppearanceLightStatusBars = true

        WindowCompat.setDecorFitsSystemWindows(window, false)


        var launchState: MainViewModel.LaunchState by mutableStateOf(MainViewModel.LaunchState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.onEach {
                    launchState = it
                }.collect()
            }
        }
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
        splashScreen.setKeepOnScreenCondition {
            when (launchState) {
                MainViewModel.LaunchState.Loading -> true
                is MainViewModel.LaunchState.Completed -> false
            }
        }
        setContent {
            VeganUniverseTheme {
                val navController = rememberNavController()
                when (val state = launchState) {
                    MainViewModel.LaunchState.Loading -> Unit
                    is MainViewModel.LaunchState.Completed -> {

                        VUApp(
                            navController = navController
                        )

                        var showOnboarding by remember { mutableStateOf(state.showOnboarding) }
                        AnimatedVisibility(
                            visible = showOnboarding,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            OnboardingScreen(onDismiss = { showOnboarding = false })
                        }

                        LaunchedEffect(Unit) {
                            mainViewModel.deeplinkFlow.collectLatest { deeplink ->
                                navController.navigate(deeplink)
                            }
                        }
                    }
                }
            }
        }
    }
}
