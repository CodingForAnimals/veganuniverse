package org.codingforanimals.veganuniverse.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorBottomAppBar
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorDestination

@Composable
internal fun VeganUniverseApp(
    navController: NavHostController,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val isInValidatorApp = remember(currentBackStackEntry) {
        currentBackStackEntry?.destination?.hierarchy?.any { hierarchyDestination ->
            hierarchyDestination.route?.contains(ValidatorDestination.ROUTE, true) == true
        } ?: false
    }

    val currentDestination = remember(currentBackStackEntry) {
        currentBackStackEntry?.destination
    }

    val currentTopLevelDestination = remember(currentBackStackEntry) {
        TopLevelDestination.fromRoute(currentBackStackEntry?.destination?.route)
    }

    VeganUniverseBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
            bottomBar = {
                Crossfade(
                    targetState = isInValidatorApp,
                    label = "bottom_bar_animation",
                ) {
                    when {
                        it -> {
                            ValidatorBottomAppBar(
                                navigateToDestination = {
                                    navController.navigate(it) {
                                        restoreState = true
                                        popUpTo(ValidatorDestination.ROUTE) {
                                            saveState = true
                                        }
                                    }
                                },
                                currentDestination = currentDestination,
                            )
                        }

                        currentTopLevelDestination != null -> {
                            VeganUniverseBottomAppBar(
                                selectedDestination = currentTopLevelDestination,
                                navigateToDestination = { destination ->
                                    navController.navigate(destination.route) {
                                        restoreState = true
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        }
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
                VUAppNavHost(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}