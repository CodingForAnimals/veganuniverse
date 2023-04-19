@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHeroColors
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel.Action
import org.codingforanimals.veganuniverse.create.presentation.place.components.EnterLocation
import org.codingforanimals.veganuniverse.create.presentation.place.components.PlaceForm
import org.codingforanimals.veganuniverse.create.presentation.place.components.SelectPlaceType
import org.codingforanimals.veganuniverse.create.presentation.place.components.SelectTags
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreatePlaceScreen(
    viewModel: CreatePlaceViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing_05),
    ) {
        item {
            ItemDetailHero(
                icon = VUIcons.PlacesFilled,
                onImageClick = {},
                colors = ItemDetailHeroColors.secondaryColors(),
            )
        }
        item {
            PlaceForm(
                name = uiState.name,
                onNameChange = { viewModel.onAction(Action.OnFormChanged(name = it)) },
                openingHours = uiState.openingHours,
                onOpeningHoursChange = { viewModel.onAction(Action.OnFormChanged(openingHours = it)) },
                description = uiState.description,
                onDescriptionChange = { viewModel.onAction(Action.OnFormChanged(description = it)) },
            )
        }
        item {
            SelectPlaceType(
                selectedPlaceType = uiState.type,
                onButtonClick = { viewModel.onAction(Action.OnFormChanged(type = it)) },
            )
        }
        item {
            EnterLocation(
                address = uiState.address,
                location = uiState.location,
                onAddressChange = { viewModel.onAction(Action.OnFormChanged(address = it)) },
                addressCandidates = uiState.addressCandidates,
                onAddressSearch = { viewModel.onAction(Action.OnAddressSearch) },
                onDialogDismissed = { viewModel.onAction(Action.OnCandidatesDialogDismissed) },
                onCandidateSelected = { viewModel.onAction(Action.OnCandidateSelected(it)) }
            )
        }
        item {
            SelectTags()
        }
    }
}

