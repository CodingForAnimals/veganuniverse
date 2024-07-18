@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.shared.RecipeCard
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipeListingScreen(
    navigateUp: () -> Unit,
    navigateToRecipeDetails: (String) -> Unit,
) {
    val viewModel: RecipeListingViewModel = koinViewModel()

    val recipes = viewModel.recipes.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { viewModel.title?.let { Text(text = stringResource(id = it)) } },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = Spacing_05,
                start = Spacing_05,
                end = Spacing_05,
                bottom = Spacing_08,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing_05),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(recipes.itemCount) { index ->
                val recipe = recipes[index] ?: return@items
                key(index) {
                    RecipeCard(
                        recipe = recipe,
                        onClick = { recipe.id?.let { navigateToRecipeDetails(it) } })
                }
            }

            recipes.loadState.apply {
                when {
                    refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                    append is LoadState.Loading -> item { CircularProgressIndicator() }
                    recipes.itemCount == 0 -> item {
                        viewModel.emptyResultsTextRes?.let {
                            ErrorView(message = it)
                        }
                    }
                }
            }
        }
    }
}
