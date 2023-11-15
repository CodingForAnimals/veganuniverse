package org.codingforanimals.veganuniverse.navigation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import org.codingforanimals.post.presentation.navigation.navigateToPost
import org.codingforanimals.post.presentation.navigation.postGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityGraph
import org.codingforanimals.veganuniverse.create.graph.CreateDestination
import org.codingforanimals.veganuniverse.create.graph.createGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.featuredTopicGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.navigateToFeaturedTopic
import org.codingforanimals.veganuniverse.notifications.presentation.navigation.notificationsGraph
import org.codingforanimals.veganuniverse.places.presentation.navigation.PlacesDestination
import org.codingforanimals.veganuniverse.places.presentation.navigation.placesGraph
import org.codingforanimals.veganuniverse.profile.ProfileDestination
import org.codingforanimals.veganuniverse.profile.profileGraph
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination
import org.codingforanimals.veganuniverse.recipes.presentation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph
import org.codingforanimals.veganuniverse.search.presentation.navigation.searchGraph
import org.codingforanimals.veganuniverse.settings.presentation.navigation.settingsGraph
import org.codingforanimals.veganuniverse.ui.navigation.Destination

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
    startDestination: Destination = CommunityDestination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        profileGraph(
            navigateToRegister = {
                navController.navigate("${RegistrationDestination.Prompt.route}/${ProfileDestination.Home.route}")
            },
            navigateToRecipe = { navController.navigate("${RecipesDestination.Details.route}/$it") },
            navigateToPlace = { navController.navigate("${PlacesDestination.Details.route}/$it") },
            navController = navController
        )
        registrationGraph(
            navController = navController,
            defaultOriginNavigationRoute = CommunityDestination.route
        )
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
            navigateToRegister = {
                navController.navigate("${RegistrationDestination.Prompt.route}/${CommunityDestination.route}")
            },
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
            navigateToAuthenticateScreen = {
                navController.navigateToAuthPromptWithOriginDestination(PlacesDestination.Details)
            }
        )
        createGraph(
            navController = navController,
            navigateToPlaceDetailsScreen = { navController.navigate("${PlacesDestination.Details.route}/$it") },
            navigateToAuthenticationScreen = {
                navController.navigateToAuthPromptWithOriginDestination(it)
            }
        )
        recipesGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigateToAuthPromptWithOriginDestination(RecipesDestination.Details)
            }
        )
    }
}

private fun NavController.navigateToAuthPromptWithOriginDestination(originDestination: Destination) {
    navigate("${RegistrationDestination.Prompt.route}/${originDestination.route}")
}

private fun NavController.navigateToCommunityPoppingBackstack() {
    navigate(CommunityDestination.route) {
//        popUpTo(RegisterDestination.route) { inclusive = true }
    }
}

class VUNavHostController(context: Context) : NavHostController(context) {
    override fun popBackStack(): Boolean {
        return when (currentDestination?.route) {
            PlacesDestination.Home.route,
            CreateDestination.Home.route,
            RecipesDestination.Home.route,
            ProfileDestination.Home.route,
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
    context: Context,
): Saver<VUNavHostController, *> = Saver(
    save = { it.saveState() },
    restore = { createNavController(context).apply { restoreState(it) } }
)

private fun createNavController(context: Context) =
    VUNavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }