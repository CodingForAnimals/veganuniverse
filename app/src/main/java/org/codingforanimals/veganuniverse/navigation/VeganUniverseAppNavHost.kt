package org.codingforanimals.veganuniverse.navigation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.map.presentation.navigation.mapGraph
import org.codingforanimals.map.presentation.navigation.navigateToMap
import org.codingforanimals.places.presentation.navigation.PlacesDestination
import org.codingforanimals.places.presentation.navigation.placesGraph
import org.codingforanimals.post.presentation.navigation.navigateToPost
import org.codingforanimals.post.presentation.navigation.postGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.navigateToCommunity
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.presentation.navigation.CreateDestination
import org.codingforanimals.veganuniverse.create.presentation.navigation.createGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.featuredTopicGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.navigateToFeaturedTopic
import org.codingforanimals.veganuniverse.notifications.presentation.navigation.notificationsGraph
import org.codingforanimals.veganuniverse.profile.presentation.navigation.ProfileDestination
import org.codingforanimals.veganuniverse.profile.presentation.navigation.profileGraph
import org.codingforanimals.veganuniverse.recipes.presentation.home.navigation.RecipeCategoriesDestination
import org.codingforanimals.veganuniverse.recipes.presentation.navigation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegisterDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.navigateToRegister
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph
import org.codingforanimals.veganuniverse.search.presentation.navigation.searchGraph
import org.codingforanimals.veganuniverse.settings.presentation.navigation.settingsGraph
import org.codingforanimals.veganuniverse.site.presentation.navigation.navigateToSite
import org.codingforanimals.veganuniverse.site.presentation.navigation.siteGraph

@Composable
internal fun rememberVeganUniverseNavController(): NavHostController {
    val context = LocalContext.current
    return rememberSaveable(
        inputs = arrayOf(context),
        saver = veganUniverseNavControllerSaver(context),
    ) { createVeganUniverseNavController(context) }
}

@Composable
internal fun VeganUniverseAppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    startDestination: Destination = CommunityDestination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        profileGraph(
            navigateToRegister = navController::navigateToRegister,
        )
        registrationGraph(
            navigateToCommunity = {
                navController.navigateToCommunity {
                    popUpTo(RegisterDestination.route) { inclusive = true }
                }
            })
        notificationsGraph(
            onBackClick = navController::navigateUp,
        )
        searchGraph(
            onBackClick = navController::navigateUp,
        )
        settingsGraph(
            onBackClick = navController::navigateUp,
        )
        communityGraph(
            navigateToFeaturedTopic = navController::navigateToFeaturedTopic,
            navigateToPost = navController::navigateToPost,
            nestedGraphs = {
                featuredTopicGraph(
                    onBackClick = navController::navigateUp,
                )
                postGraph()
            }
        )
        placesGraph(
            snackbarHostState = snackbarHostState,
            cameraPositionState = cameraPositionState,
            navigateToMap = navController::navigateToMap,
        )
        createGraph()
        mapGraph(
            navigateToSite = navController::navigateToSite,
            snackbarHostState = snackbarHostState,
            nestedGraphs = {
                siteGraph()
            }
        )
        recipesGraph(
            navController = navController,
        )
    }
}

class VeganUniverseNavHostController(context: Context) : NavHostController(context) {
    override fun popBackStack(): Boolean {
        return when (currentDestination?.route) {
            PlacesDestination.route,
            CreateDestination.route,
            RecipeCategoriesDestination.route,
            ProfileDestination.route,
            -> {
                navigate(CommunityDestination.route) {
                    popUpTo(CommunityDestination.route) { inclusive = true }
                }
                true
            }
            else -> super.popBackStack()
        }
    }
}

private fun veganUniverseNavControllerSaver(
    context: Context
): Saver<VeganUniverseNavHostController, *> = Saver(
    save = { it.saveState() },
    restore = { createVeganUniverseNavController(context).apply { restoreState(it) } }
)

private fun createVeganUniverseNavController(context: Context) =
    VeganUniverseNavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }