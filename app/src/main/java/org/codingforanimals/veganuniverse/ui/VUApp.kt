@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.ui

import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavHostController
import org.codingforanimals.veganuniverse.navigation.VUAppNavHost
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.navbar.VUBottomNavBar
import org.codingforanimals.veganuniverse.ui.topappbar.VUTopAppBar

@Composable
internal fun VUApp(
    navController: NavHostController,
    appState: VUAppState = rememberVUAppState(navController),
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
                    onBackClick = appState::navigateBackHomeToProducts,
                )

                VUAppNavHost(
                    navController = appState.navController,
                )
            }
        }
    }
}