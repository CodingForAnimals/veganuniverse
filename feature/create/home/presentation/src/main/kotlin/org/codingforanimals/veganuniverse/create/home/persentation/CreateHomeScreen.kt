package org.codingforanimals.veganuniverse.create.home.persentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeViewModel.Action
import org.codingforanimals.veganuniverse.create.home.persentation.components.CreateContentSelectionCard
import org.codingforanimals.veganuniverse.create.home.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateHomeScreen() {

    val viewModel: CreateHomeViewModel = koinViewModel()

    CreateHomeScreen(
        onAction = viewModel::onAction,
    )
}

@Composable
private fun CreateHomeScreen(
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_05)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing_05)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing_06)
        ) {
            CreateContentSelectionCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                title = stringResource(R.string.places),
                image = R.drawable.img_create_place,
                onClick = { onAction(Action.OnCreatePlaceClick) }
            )
            CreateContentSelectionCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                title = stringResource(R.string.recipes),
                image = R.drawable.img_create_recipe,
                onClick = { onAction(Action.OnCreateRecipeClick) }
            )
        }
        CreateContentSelectionCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            title = stringResource(R.string.product),
            image = R.drawable.img_create_product,
            onClick = { onAction(Action.OnCreateProductClick) },
        )
    }
}
