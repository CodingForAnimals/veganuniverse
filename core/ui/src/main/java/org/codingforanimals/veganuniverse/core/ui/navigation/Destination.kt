package org.codingforanimals.veganuniverse.core.ui.navigation

import androidx.navigation.NavController

abstract class Destination(val route: String)

fun NavController.navigate(destination: Destination) = navigate(destination.route)