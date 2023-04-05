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
import org.codingforanimals.places.presentation.home.PlacesHomeDestination
import org.codingforanimals.places.presentation.placesGraph
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
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeDestination
import org.codingforanimals.veganuniverse.recipes.presentation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegisterDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.navigateToRegister
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph
import org.codingforanimals.veganuniverse.search.presentation.navigation.searchGraph
import org.codingforanimals.veganuniverse.settings.presentation.navigation.settingsGraph

@Composable
internal fun rememberVUNavController(): NavHostController {
    val context = LocalContext.current
    return rememberSaveable(
        inputs = arrayOf(context),
        saver = navControllerSaver(context),
    ) { createNavController(context) }
}

@Composable
internal fun VUAppNavHost(
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
            navController = navController,
            snackbarHostState = snackbarHostState,
            cameraPositionState = cameraPositionState,
        )
        createGraph()
        recipesGraph(
            navController = navController,
        )
    }
}

class VUNavHostController(context: Context) : NavHostController(context) {
    override fun popBackStack(): Boolean {
        return when (currentDestination?.route) {
            PlacesHomeDestination.route,
            CreateDestination.route,
            RecipesHomeDestination.route,
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

private fun navControllerSaver(
    context: Context
): Saver<VUNavHostController, *> = Saver(
    save = { it.saveState() },
    restore = { createNavController(context).apply { restoreState(it) } }
)

private fun createNavController(context: Context) =
    VUNavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }