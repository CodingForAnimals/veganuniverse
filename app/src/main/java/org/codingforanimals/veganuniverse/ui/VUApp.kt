@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.navigation.VUAppNavHost
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.navbar.VUBottomNavBar
import org.codingforanimals.veganuniverse.ui.topappbar.VUTopAppBar

@Composable
internal fun VUApp(
    appState: VUAppState = rememberVUAppState(),
) {
    VeganUniverseBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val topLevelDestination = appState.currentTopLevelDestination
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
            bottomBar = {
                VUBottomNavBar(
                    visible = topLevelDestination != null,
                    destinations = appState.topLevelDestinations,
                    currentDestination = appState.currentDestination,
                    navigateToDestination = appState::navigateToTopLevelDestination
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .imePadding()
                    .safeDrawingPadding()
            ) {
                VUTopAppBar(
                    topLevelDestination = topLevelDestination,
                    onBackClick = appState::navigateToCommunity,
                    actions = appState.topBarActions,
                    onActionClick = appState::onActionClick
                )

                VUAppNavHost(
                    navController = appState.navController,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}