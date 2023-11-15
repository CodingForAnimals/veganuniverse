@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.home.persentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.create.home.persentation.components.CreateContentSelectionCard
import org.codingforanimals.veganuniverse.create.home.presentation.R
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateHomeScreen(
    navigateToAuthenticationScreen: () -> Unit,
    navigateToCreatePlaceScreen: () -> Unit,
    navigateToCreateRecipeScreen: () -> Unit,
    viewModel: CreateHomeViewModel = koinViewModel(),
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
                    image = R.drawable.create_place_img,
                    onClick = navigateToCreatePlaceScreen
                )
                CreateContentSelectionCard(
                    title = stringResource(R.string.recipes),
                    image = R.drawable.create_recipe_img,
                    onClick = navigateToCreateRecipeScreen
                )
                CreateContentSelectionCard(
                    title = stringResource(R.string.post),
                    image = R.drawable.create_post_img,
                    onClick = {}
                )
            }
        }
    }
}
