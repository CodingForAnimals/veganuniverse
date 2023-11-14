package org.codingforanimals.veganuniverse.community.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.community.presentation.CommunityScreen
import org.codingforanimals.veganuniverse.ui.navigation.Destination

object CommunityDestination : Destination(route = "community_route")

fun NavGraphBuilder.communityGraph(
    navigateToRegister: () -> Unit,
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = CommunityDestination.route) {
        CommunityScreen(
            navigateToRegister = navigateToRegister,
            navigateToFeaturedTopic = navigateToFeaturedTopic,
            navigateToPost = navigateToPost,
        )
    }
    nestedGraphs()
}
