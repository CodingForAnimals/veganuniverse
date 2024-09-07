package org.codingforanimals.veganuniverse.commons.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

fun NavDestination?.isRouteInHierarchy(route: String) =
    this?.hierarchy?.any {
        it.route?.contains(route, true) ?: false
    } ?: false

fun NavController.popToStartDestination() {
    popBackStack(graph.startDestinationId, false)
}
