package org.codingforanimals.places.presentation.details

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object PlaceDetailsDestination : Destination(route = "place_details_route")

fun NavGraphBuilder.placeDetailsGraph(
    onBackClick: () -> Unit,
) {
    composable(
        route = PlaceDetailsDestination.route,
    ) {
        PlaceDetailsScreen(
            onBackClick = onBackClick,
        )
    }
}