package org.codingforanimals.veganuniverse.ui.navhost

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.map.presentation.navigation.mapGraph
import org.codingforanimals.map.presentation.navigation.navigateToMap
import org.codingforanimals.places.presentation.navigation.placesGraph
import org.codingforanimals.post.presentation.navigation.navigateToPost
import org.codingforanimals.post.presentation.navigation.postGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.navigateToCommunity
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.presentation.navigation.createGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.featuredTopicGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.navigateToFeaturedTopic
import org.codingforanimals.veganuniverse.notifications.presentation.navigation.notificationsGraph
import org.codingforanimals.veganuniverse.presentation.navigation.recipesGraph
import org.codingforanimals.veganuniverse.profile.presentation.navigation.profileGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegisterDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.navigateToRegister
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph
import org.codingforanimals.veganuniverse.search.presentation.navigation.searchGraph
import org.codingforanimals.veganuniverse.site.presentation.navigation.navigateToSite
import org.codingforanimals.veganuniverse.site.presentation.navigation.siteGraph

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
        communityGraph(
            navigateToFeaturedTopic = navController::navigateToFeaturedTopic,
            navigateToPost = navController::navigateToPost,
            nestedGraphs = {
                featuredTopicGraph()
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
        recipesGraph()
    }
}