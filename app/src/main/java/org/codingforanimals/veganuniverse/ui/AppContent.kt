@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons

@Composable
internal fun AppContent(
    appState: VeganUniverseAppState,
    showNavBar: Boolean,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        bottomBar = {
            VeganUniverseBottomNavBar(
                showNavBar = showNavBar,
                destinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                navigateToDestination = appState::navigateToTopLevelDestination
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val topLevelDestination = appState.currentTopLevelDestination
                AnimatedVisibility(visible = topLevelDestination != null) {
                    VeganUniverseTopAppBar(
                        titleRes = topLevelDestination?.titleTextId
                            ?: R.string.empty_string,
                        actionIcon = VeganUniverseIcons.Profile,
                    )
                }
                VeganUniverseAppNavHost(
                    navController = appState.navController,
                    snackbarHostState = snackbarHostState,
                    cameraPositionState = appState.cameraPositionState,
                )
            }
        }
    }
}