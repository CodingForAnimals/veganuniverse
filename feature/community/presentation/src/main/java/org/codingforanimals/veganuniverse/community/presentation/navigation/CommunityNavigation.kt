package org.codingforanimals.veganuniverse.community.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.community.presentation.CommunityScreen

const val communityNavigationRoute = "community_route"

fun NavController.navigateToCommunity() = navigate(communityNavigationRoute)

fun NavGraphBuilder.communityGraph(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = communityNavigationRoute) {
        CommunityScreen(
            navigateToFeaturedTopic = navigateToFeaturedTopic,
            navigateToPost = navigateToPost,
        )
    }
    nestedGraphs()
}
