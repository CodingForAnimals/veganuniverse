@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.navigation

import androidx.compose.animation.Crossfade
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorBottomAppBar
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorDestination
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorTopAppBar

@Composable
internal fun VeganUniverseApp(
    navController: NavHostController,
    appState: VUAppState = rememberVUAppState(navController),
) {
    val isInValidatorApp =
        appState.currentDestination?.hierarchy?.any { hierarchyDestination ->
            hierarchyDestination.route?.contains(ValidatorDestination.ROUTE, true) == true
        } ?: false
    VeganUniverseBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val topLevelDestination = appState.currentTopLevelDestination
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
            bottomBar = {
                Crossfade(isInValidatorApp, label = "bottom_bar_animation") {
                    if (it) {
                        ValidatorBottomAppBar(
                            navController = navController,
                            currentDestination = appState.currentDestination,
                        )
                    } else {
                        VeganUniverseBottomAppBar(
                            visible = topLevelDestination != null,
                            destinations = appState.topLevelDestinations,
                            currentDestination = appState.currentDestination,
                            navigateToDestination = appState::navigateToTopLevelDestination
                        )
                    }
                }
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
                Crossfade(isInValidatorApp, label = "bottom_bar_animation") {
                    if (it) {
                        ValidatorTopAppBar(
                            navController = navController,
                        )
                    } else {
                        VUTopAppBar(
                            topLevelDestination = topLevelDestination,
                            onBackClick = appState::navigateBackHomeToProducts,
                        )
                    }
                }

                VUAppNavHost(
                    navController = appState.navController,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}