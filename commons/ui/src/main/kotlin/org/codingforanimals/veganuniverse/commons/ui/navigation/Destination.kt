package org.codingforanimals.veganuniverse.commons.ui.navigation

import androidx.navigation.NavController

abstract class Destination(open val route: String)

fun NavController.navigate(destination: Destination) {
    navigate(destination.route)
}
