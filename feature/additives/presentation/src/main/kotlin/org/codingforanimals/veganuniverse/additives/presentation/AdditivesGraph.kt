package org.codingforanimals.veganuniverse.additives.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.additives.presentation.browsing.AdditivesBrowsingScreen
import org.codingforanimals.veganuniverse.additives.presentation.detail.AdditiveDetailScreen
import org.codingforanimals.veganuniverse.additives.presentation.detail.edit.AdditiveDetailEditScreen
import org.codingforanimals.veganuniverse.additives.presentation.validator.detail.AdditiveEditValidationScreen
import org.codingforanimals.veganuniverse.additives.presentation.validator.list.AdditiveEditListScreen
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate

sealed class AdditivesDestination(override val route: String) : Destination(route) {
    data object Browsing : AdditivesDestination("$ADDITIVES/browsing")
    data class Detail(val id: String) : AdditivesDestination("$PATH/$id") {
        companion object {
            const val ARG_ID = "id"
            const val PATH = "$ADDITIVES/detail"
            const val APP_LINK = "${DeepLink.APP_LINKS_BASE_URL}/additive"
            fun getAppLink(additiveID: String) = "$APP_LINK/$additiveID"
        }
    }
    data class Edit(val id: String) : AdditivesDestination("$PATH/$id") {
        companion object {
            const val ARG_ID = "id"
            const val PATH = "$ADDITIVES/edit"
        }
    }

    data object EditList : AdditivesDestination("$ADDITIVES/edit-list")

    data class EditValidation(val editID: String) : AdditivesDestination("$PATH/$editID") {
        companion object {
            const val ARG_EDIT_ID = "editID"
            const val PATH = "$ADDITIVES/edit-validate"
        }
    }

    companion object {
        private const val ADDITIVES = "additives"
    }
}

fun NavGraphBuilder.additivesGraph(
    navController: NavController,
) {
    with(AdditivesDestination.Browsing) {
        composable(route) {
            AdditivesBrowsingScreen(
                navigateUp = navController::navigateUp,
                navigateToAdditiveDetail = { navController.navigate(AdditivesDestination.Detail(it)) }
            )
        }
    }

    with(AdditivesDestination.Detail) {
        composable(
            route = "$PATH/{$ARG_ID}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$APP_LINK/{$ARG_ID}"
                }
            )
        ) {
            AdditiveDetailScreen(
                navigateUp = navController::navigateUp,
                navigateToEditAdditive = { navController.navigate(AdditivesDestination.Edit(it)) }
            )
        }
    }

    with(AdditivesDestination.Edit) {
        composable(
            route = "$PATH/{$ARG_ID}"
        ) {
            AdditiveDetailEditScreen(
                navigateUp = navController::navigateUp,
            )
        }
    }

    with(AdditivesDestination.EditValidation) {
        composable(
            route = "$PATH/{$ARG_EDIT_ID}",
        ) {
            AdditiveEditValidationScreen(
                navigateUp = navController::navigateUp,
            )
        }
    }

    with(AdditivesDestination.EditList) {
        composable(
            route = route
        ) {
            AdditiveEditListScreen(
                navigateToAdditiveEditValidation = { navController.navigate(AdditivesDestination.EditValidation(it)) },
                navigateUp = navController::navigateUp,
            )
        }
    }
}