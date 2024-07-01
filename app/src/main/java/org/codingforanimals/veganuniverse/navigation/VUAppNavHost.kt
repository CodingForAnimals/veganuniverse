package org.codingforanimals.veganuniverse.navigation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.graph.CreateDestination
import org.codingforanimals.veganuniverse.create.graph.createGraph
import org.codingforanimals.veganuniverse.place.presentation.navigation.PlaceDestination
import org.codingforanimals.veganuniverse.place.presentation.navigation.placesGraph
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.product.presentation.navigation.productGraph
import org.codingforanimals.veganuniverse.profile.ProfileDestination
import org.codingforanimals.veganuniverse.profile.profileGraph
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination
import org.codingforanimals.veganuniverse.recipes.presentation.recipesGraph
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination
import org.codingforanimals.veganuniverse.registration.presentation.navigation.registrationGraph

@Composable
internal fun rememberVUNavController(): NavHostController {
    val context = LocalContext.current
    return rememberSaveable(
        inputs = arrayOf(context),
        saver = navControllerSaver(context),
    ) { createNavController(context) }
}

@Composable
internal fun VUAppNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startDestination: Destination = ProductDestination.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        profileGraph(
            navigateToRegister = {
                navController.navigateToAuthPromptWithOriginDestination(ProfileDestination.Home)
            },
//            navigateToRecipe = { navController.navigate("${RecipesDestination.Details.route}/$it") },
//            navigateToPlace = { navController.navigate("${PlaceDestination.Details.route}/$it") },
            navController = navController,
        )
        productGraph(
            navController = navController,
            navigateToCreateProductScreen = { navController.navigate(CreateDestination.Product.route) },
            snackbarHostState = snackbarHostState,
            navigateToAuthScreen = {
                navController.navigateToAuthPromptWithOriginDestination(it)
            }
        )
        registrationGraph(
            navController = navController,
            defaultOriginNavigationRoute = ProductDestination.Home.route
        )
        placesGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigateToAuthPromptWithOriginDestination(PlaceDestination.Details)
            },
            navigatoToReauthenticateScreen = {
                navController.navigate(RegistrationDestination.EmailReauthentication.route)
            }
        )
        createGraph(
            navController = navController,
            navigateToAuthenticationScreen = {
                navController.navigateToAuthPromptWithOriginDestination(it)
            }
        )
        recipesGraph(
            navController = navController,
            navigateToAuthenticateScreen = {
                navController.navigateToAuthPromptWithOriginDestination(RecipesDestination.Details)
            }
        )
    }
}

private fun NavController.navigateToAuthPromptWithOriginDestination(originDestination: Destination) {
    navigate("${RegistrationDestination.Prompt.route}/${originDestination.route}")
}

class VUNavHostController(context: Context) : NavHostController(context) {
    override fun popBackStack(): Boolean {
        return when (currentDestination?.route) {
            PlaceDestination.Home.route,
            CreateDestination.Home.route,
            RecipesDestination.Home.route,
            ProfileDestination.Home.route,
            -> {
                navigate(ProductDestination.Home.route) {
                    popUpTo(ProductDestination.Home.route) { inclusive = true }
                }
                true
            }

            else -> super.popBackStack()
        }
    }
}

private fun navControllerSaver(
    context: Context,
): Saver<VUNavHostController, *> = Saver(
    save = { it.saveState() },
    restore = { createNavController(context).apply { restoreState(it) } }
)

private fun createNavController(context: Context) =
    VUNavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }