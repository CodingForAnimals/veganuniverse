package org.codingforanimals.veganuniverse.search.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.search.presentation.SearchScreen

object SearchDestination : Destination(route = "search_route")

fun NavGraphBuilder.searchGraph(
    onBackClick: () -> Unit,
) {
    composable(
        route = SearchDestination.route
    ) {
        SearchScreen(
            onBackClick = onBackClick,
        )
    }
}