@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.home.persentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeViewModel.Action
import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.create.home.persentation.components.CreateContentSelectionCard
import org.codingforanimals.veganuniverse.create.home.presentation.R
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateHomeScreen(
    navigateToAuthenticationScreen: () -> Unit,
    navigateToCreatePlaceScreen: () -> Unit,
    navigateToCreateRecipeScreen: () -> Unit,
    navigateToCreateProductScreen: () -> Unit,
    viewModel: CreateHomeViewModel = koinViewModel(),
) {

    CreateHomeScreen(
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToAuthenticationScreen = navigateToAuthenticationScreen,
        navigateToCreatePlaceScreen = navigateToCreatePlaceScreen,
        navigateToCreateRecipeScreen = navigateToCreateRecipeScreen,
        navigateToCreateProductScreen = navigateToCreateProductScreen,
    )
}

@Composable
private fun CreateHomeScreen(
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing_06),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing_06,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                CreateContentSelectionCard(
                    title = stringResource(R.string.places),
                    image = R.drawable.img_create_place,
                    onClick = { onAction(Action.OnCreatePlaceClick) }
                )
                CreateContentSelectionCard(
                    title = stringResource(R.string.recipes),
                    image = R.drawable.img_create_recipe,
                    onClick = { onAction(Action.OnCreateRecipeClick) }
                )
                CreateContentSelectionCard(
                    title = stringResource(R.string.product),
                    image = R.drawable.img_create_post,
                    onClick = { onAction(Action.OnCreateProductClick) },
                )
            }
        }
    }
}

@Composable
fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToAuthenticationScreen: () -> Unit,
    navigateToCreatePlaceScreen: () -> Unit,
    navigateToCreateRecipeScreen: () -> Unit,
    navigateToCreateProductScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateToAuthenticateScreen -> navigateToAuthenticationScreen()
                SideEffect.NavigateToCreatePlace -> navigateToCreatePlaceScreen()
                SideEffect.NavigateToCreateProduct -> navigateToCreateProductScreen()
                SideEffect.NavigateToCreateRecipe -> navigateToCreateRecipeScreen()
            }
        }.collect()
    }
}