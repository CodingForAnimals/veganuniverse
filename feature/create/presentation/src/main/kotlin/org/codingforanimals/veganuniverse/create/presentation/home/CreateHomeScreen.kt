@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.topbar.HomeScreenTopAppBar
import org.codingforanimals.veganuniverse.create.presentation.R
import org.codingforanimals.veganuniverse.create.presentation.home.CreateHomeViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.home.components.CreateContentSelectionCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateHomeScreen(
    navigateUp: () -> Unit,
) {

    val viewModel: CreateHomeViewModel = koinViewModel()

    CreateHomeScreen(
        onAction = viewModel::onAction,
        navigateUp = navigateUp,
    )
}

@Composable
private fun CreateHomeScreen(
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit = {},
    navigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeScreenTopAppBar(
                title = stringResource(id = R.string.create_home_title),
                onBackClick = navigateUp,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing_06, vertical = Spacing_06)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing_05)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                CreateContentSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    title = stringResource(R.string.create_place_card_label),
                    image = R.drawable.img_create_place,
                    onClick = { onAction(Action.OnCreatePlaceClick) }
                )
                CreateContentSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    title = stringResource(R.string.create_recipes_card_label),
                    image = R.drawable.img_create_recipe,
                    onClick = { onAction(Action.OnCreateRecipeClick) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                CreateContentSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    title = stringResource(R.string.create_product_card_label),
                    image = R.drawable.img_create_product,
                    onClick = { onAction(Action.OnCreateProductClick) },
                )
                CreateContentSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    title = stringResource(R.string.create_additive_card_label),
                    image = R.drawable.img_create_additive,
                    onClick = { onAction(Action.OnCreateAdditiveClick) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateHomeScreen() {
    VeganUniverseTheme {
        CreateHomeScreen()
    }
}