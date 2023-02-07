package org.codingforanimals.veganuniverse.site.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.site.presentation.SiteScreen

object SiteDestination : Destination(route = "site_route")

fun NavController.navigateToSite() = navigate(SiteDestination.route)

fun NavGraphBuilder.siteGraph() {
    composable(
        route = SiteDestination.route,
        content = {
            SiteScreen()
        }
    )
}