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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import org.codingforanimals.veganuniverse.app.R
import org.codingforanimals.veganuniverse.core.ui.components.BottomNavBar
import org.codingforanimals.veganuniverse.core.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.DrawableResourceIcon
import org.codingforanimals.veganuniverse.core.ui.icons.Icon.ImageVectorIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingScreen
import org.codingforanimals.veganuniverse.ui.theme.VeganUniverseTheme

@Composable
internal fun VeganUniverseApp(
    appState: VeganUniverseAppState = rememberVeganUniverseAppState()
) {
    VeganUniverseBackground {

        var showOnboarding by remember { mutableStateOf(true) }
        if (showOnboarding) {
            OnboardingScreen { showOnboarding = false }
        } else {
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                bottomBar = {
                    VeganUniverseBottomNavBar(
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
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VeganUniverseBottomNavBar(
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination>,
    navigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    BottomNavBar(modifier = modifier) {
        destinations.forEach {
            val isSelected = currentDestination.isTopLevelDestinationInHierarchy(it)
            BottomNavBarItem(
                isSelected = isSelected,
                onClick = { if (!isSelected) navigateToDestination(it) },
                icon = {
                    val icon = if (isSelected) {
                        it.selectedIcon
                    } else {
                        it.unselectedIcon
                    }
                    when (icon) {
                        is ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = stringResource(it.iconTextId),
                        )
                        is DrawableResourceIcon -> Icon(
                            painter = painterResource(icon.id),
                            contentDescription = stringResource(it.iconTextId),
                        )
                    }
                },
                label = { Text(stringResource(it.iconTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val appState = rememberVeganUniverseAppState()
    VeganUniverseTheme {
        val snackbarHostState = SnackbarHostState()
        VeganUniverseAppNavHost(
            navController = appState.navController,
            snackbarHostState = snackbarHostState,
        )
    }
}

