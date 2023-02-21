@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.ui

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
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.navbar.VeganUniverseBottomNavBar
import org.codingforanimals.veganuniverse.ui.navhost.VeganUniverseAppNavHost
import org.codingforanimals.veganuniverse.ui.topappbar.VeganUniverseTopAppBar

@Composable
internal fun VeganUniverseApp(
    appState: VeganUniverseAppState = rememberVeganUniverseAppState(),
) {
    VeganUniverseBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val topLevelDestination = appState.currentTopLevelDestination
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
            bottomBar = {
                VeganUniverseBottomNavBar(
                    visible = topLevelDestination != null,
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
                    VeganUniverseTopAppBar(
                        topLevelDestination = topLevelDestination,
                        onBackClick = appState::navigateBackToCommunity,
                        actions = appState.topBarActions,
                        onActionClick = appState::onActionClick
                    )

                    VeganUniverseAppNavHost(
                        navController = appState.navController,
                        snackbarHostState = snackbarHostState,
                        cameraPositionState = appState.cameraPositionState,
                    )
                }
            }
        }
    }
}