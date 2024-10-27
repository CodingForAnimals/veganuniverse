package org.codingforanimals.veganuniverse.app.validate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.place.presentation.validate.ValidatePlacesScreen
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.product.presentation.validate.ValidateProductsScreen

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
    navigateToAdditiveEdits: () -> Unit,
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
                navigateToAdditiveEdits = navigateToAdditiveEdits,
                onBackClick = {
                    navController.exitValidator()
                },
                navigateToProductDetail = {
                    navController.navigate(ProductDestination.UnvalidatedDetail(it))
                },
                navigateToCompareProductEdit = { editId, originalId ->
                    navController.navigate(ProductDestination.CompareEdit(editId, originalId))
                }
            )
        }

        composable(
            route = ValidatorDestination.ValidatePlaces.route,
        ) {
            ValidatePlacesScreen(
                onBackClick = {
                    navController.exitValidator()
                }
            )
        }
    }
}
