package org.codingforanimals.places.presentation.home.bottomsheet

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.entity.PlaceViewEntity
import org.codingforanimals.places.presentation.home.PlacesHomeViewModel
import org.codingforanimals.places.presentation.home.PlacesHomeViewModel.Action
import org.codingforanimals.places.presentation.home.composables.FilterDialog
import org.codingforanimals.places.presentation.home.composables.PlaceCard
import org.codingforanimals.places.presentation.home.composables.SortDialog
import org.codingforanimals.places.presentation.home.state.FilterState
import org.codingforanimals.places.presentation.home.state.PlacesState
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.core.ui.components.VUImage
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.icons.VUImages
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun SuccessSheet(
    placesState: PlacesState.Success,
    filterState: FilterState,
    selectedPlace: PlaceViewEntity?,
    isFocused: Boolean,
    onAction: (Action) -> Unit,
) {
    Crossfade(
        modifier = Modifier.animateContentSize(),
        targetState = isFocused,
    ) {
        if (it && selectedPlace != null) {
            val selectedPlaceCardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            PlaceCard(
                placeViewEntity = selectedPlace,
                onCardClick = { onAction(Action.OnPlaceClick(selectedPlace)) },
                border = selectedPlaceCardBorder,
            )
        } else {
            Column {
                SortAndFilter(filterState, onAction)
                Crossfade(targetState = placesState.content.isEmpty()) { empty ->
                    if (empty) {
                        EmptySuccessSheet()
                    } else {
                        ContentSuccessSheet(
                            placesState = placesState,
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
    when (filterState.visibleDialog) {
        null -> Unit
        PlacesHomeViewModel.FilterDialog.Filter -> {
            FilterDialog(
                activePlaceType = filterState.activePlaceType,
                activeFilterTags = filterState.activePlaceTags,
                onDismissDialogRequest = { onAction(Action.OnFilterDialogDismissRequest) },
                onFilterRequest = { type, tags -> onAction(Action.OnFilterRequest(type, tags)) },
            )
        }
        PlacesHomeViewModel.FilterDialog.Sort -> {
            SortDialog(
                activeSorter = filterState.sorter,
                onDismissDialogRequest = { onAction(Action.OnFilterDialogDismissRequest) },
                onSortRequest = { onAction(Action.OnSortRequest(it)) },
            )
        }
    }
}

@Composable
private fun SortAndFilter(
    filterState: FilterState,
    onAction: (Action) -> Unit,
) {
    Row(
        modifier = Modifier.padding(horizontal = Spacing_04),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        VUAssistChip(
            icon = VUIcons.Filter,
            label = "Filtrar",
            onClick = { onAction(Action.OnFilterChipClick) },
            iconDescription = "",
            colors = if (filterState.isFilterActive) VUAssistChipDefaults.primarySelectedColors() else VUAssistChipDefaults.primaryColors()
        )
        VUAssistChip(
            icon = VUIcons.Sort,
            label = "Ordenar",
            onClick = { onAction(Action.OnSortChipClick) },
            iconDescription = ""
        )
    }
}

@Composable
private fun ContentSuccessSheet(
    placesState: PlacesState.Success,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        content = {
            items(
                items = placesState.content,
                itemContent = { place ->
                    PlaceCard(
                        placeViewEntity = place,
                        onCardClick = { onAction(Action.OnPlaceClick(place)) },
                    )
                }
            )
        },
    )
}

@Composable
private fun EmptySuccessSheet() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VUImage(
            modifier = Modifier
                .size(150.dp),
            image = VUImages.ErrorCat,
        )
        Text(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(bottom = Spacing_06),
            textAlign = TextAlign.Center,
            text = "¡No encontramos lugares en tu búsqueda! El gato se aburre y rompe más cosas"
        )
    }
}