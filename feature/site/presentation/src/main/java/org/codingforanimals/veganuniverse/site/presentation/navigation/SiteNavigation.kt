package org.codingforanimals.veganuniverse.site.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.site.presentation.SiteScreen

private const val siteNavigationRoute = "site_route"

fun NavController.navigateToSite() = navigate(siteNavigationRoute)

fun NavGraphBuilder.siteGraph() {
    composable(
        route = siteNavigationRoute,
        content = {
            SiteScreen()
        }
    )
}