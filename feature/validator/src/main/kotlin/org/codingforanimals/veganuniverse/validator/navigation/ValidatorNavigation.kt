package org.codingforanimals.veganuniverse.validator.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.validator.place.presentation.ValidatePlacesScreen
import org.codingforanimals.veganuniverse.validator.product.presentation.ValidateProductsScreen

sealed class ValidatorDestination(override val route: String) : Destination(route) {
    data object ValidateProducts : ValidatorDestination("${ROUTE}_validate_products")
    data object ValidatePlaces : ValidatorDestination("${ROUTE}_validate_places")

    companion object {
        const val ROUTE = "validator"
    }
}

private fun NavController.exitValidator() {
    popBackStack(ValidatorDestination.ROUTE, true)
}

fun NavGraphBuilder.validatorNavigation(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    navigation(
        route = ValidatorDestination.ROUTE,
        startDestination = ValidatorDestination.ValidateProducts.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.Validator.deeplink
            }
        )
    ) {
        composable(
            route = ValidatorDestination.ValidateProducts.route,
        ) {
            ValidateProductsScreen(
                snackbarHostState = snackbarHostState,
                onBackClick = {
                    navController.exitValidator()
                }
            )
        }

        composable(
            route = ValidatorDestination.ValidatePlaces.route,
        ) {
            ValidatePlacesScreen(
                snackbarHostState = snackbarHostState,
                onBackClick = {
                    navController.exitValidator()
                }
            )
        }
    }
}