package org.codingforanimals.veganuniverse.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.codingforanimals.map.presentation.navigation.mapGraph
import org.codingforanimals.post.presentation.navigation.navigateToPost
import org.codingforanimals.post.presentation.navigation.postGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityGraph
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityNavigationRoute
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.featuredTopicGraph
import org.codingforanimals.veganuniverse.featuredtopic.presentation.nav.navigateToFeaturedTopic
import org.codingforanimals.veganuniverse.presentation.navigation.recipesGraph
import org.codingforanimals.veganuniverse.site.presentation.navigation.navigateToSite
import org.codingforanimals.veganuniverse.site.presentation.navigation.siteGraph

@Composable
internal fun VeganUniverseAppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = communityNavigationRoute,
    ) {
        communityGraph(
            navigateToFeaturedTopic = navController::navigateToFeaturedTopic,
            navigateToPost = navController::navigateToPost,
            nestedGraphs = {
                featuredTopicGraph()
                postGraph()
            }
        )
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