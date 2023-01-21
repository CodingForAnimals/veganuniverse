@file:OptIn(ExperimentalComposeUiApi::class)

package org.codingforanimals.veganuniverse.site.presentation.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
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