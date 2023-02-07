package org.codingforanimals.veganuniverse.community.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.community.presentation.CommunityScreen
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object CommunityDestination : Destination(route = "community_route")

fun NavController.navigateToCommunity(navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}) =
    navigate(CommunityDestination.route, navOptionsBuilder)

fun NavGraphBuilder.communityGraph(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = CommunityDestination.route) {
        CommunityScreen(
            navigateToFeaturedTopic = navigateToFeaturedTopic,
            navigateToPost = navigateToPost,
        )
    }
    nestedGraphs()
}
