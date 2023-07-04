package org.codingforanimals.veganuniverse.community.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.community.presentation.CommunityScreen
import org.codingforanimals.veganuniverse.community.presentation.di.communityModule
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.koin.core.context.loadKoinModules

object CommunityDestination : Destination(route = "community_route")

fun NavGraphBuilder.communityGraph(
    navigateToRegister: () -> Unit,
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    loadKoinModules(communityModule)
    composable(route = CommunityDestination.route) {
        CommunityScreen(
            navigateToRegister = navigateToRegister,
            navigateToFeaturedTopic = navigateToFeaturedTopic,
            navigateToPost = navigateToPost,
        )
    }
    nestedGraphs()
}
