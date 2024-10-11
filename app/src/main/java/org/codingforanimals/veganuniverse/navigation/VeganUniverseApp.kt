package org.codingforanimals.veganuniverse.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorBottomAppBar
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorDestination

@Composable
internal fun VeganUniverseApp(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
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

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            when {
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

                isInValidatorApp -> {
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
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        VUAppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .padding(padding)
                .imePadding()
            ,
            navController = navController,
        )
    }
}
