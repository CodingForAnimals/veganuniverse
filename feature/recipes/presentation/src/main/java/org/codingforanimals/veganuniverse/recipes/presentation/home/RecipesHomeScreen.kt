package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarousel
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainer
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipesHomeScreen(
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateToRecipe: (String) -> Unit,
    viewModel: RecipesHomeViewModel = koinViewModel(),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_06)
    ) {
        items(viewModel.content) { item ->
            when (item) {
                is RecipeHomeScreenItem.Carousel -> RecipeCarousel(
                    sorter = item.sorter,
                    navigateToRecipe = navigateToRecipe,
                    navigateToRecipeBrowsing = navigateToRecipeBrowsing,
                )

                is RecipeHomeScreenItem.Container -> RecipeTagContainer(
                    tag = item.tag,
                    layoutType = item.layoutType,
                    navigateToRecipe = navigateToRecipe,
                    navigateToRecipeBrowsing = navigateToRecipeBrowsing
                )
            }
        }
    }
}
