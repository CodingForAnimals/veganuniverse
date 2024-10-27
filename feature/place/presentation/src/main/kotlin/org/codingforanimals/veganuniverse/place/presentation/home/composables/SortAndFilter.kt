@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.place.presentation.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.commons.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.place.presentation.model.label
import org.codingforanimals.veganuniverse.place.presentation.model.toUI
import org.codingforanimals.veganuniverse.place.shared.model.PlaceCardSorter
import org.codingforanimals.veganuniverse.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.place.shared.model.PlaceType

@Composable
internal fun FilterDialog(
    activePlaceType: PlaceType?,
    activeFilterTags: List<PlaceTag>,
    onDismissDialogRequest: () -> Unit,
    onFilterRequest: (PlaceType?, List<PlaceTag>) -> Unit,
) {
    var selectedPlaceType by remember { mutableStateOf(activePlaceType) }
    var selectedPlaceTags by remember { mutableStateOf(activeFilterTags) }
    val onTagClick = { tag: PlaceTag ->
        val list = selectedPlaceTags.toMutableList()
        if (!list.remove(tag)) {
            list.add(tag)
        }
        selectedPlaceTags = list
    }
    Dialog(onDismissRequest = onDismissDialogRequest) {
        Card {
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(Spacing_05),
                    text = "Filtrar por",
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    modifier = Modifier.padding(top = Spacing_05, start = Spacing_05),
                    text = "Tipo de lugar"
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = Spacing_05),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_03)
                ) {
                    val onTypeClick = { type: PlaceType ->
                        selectedPlaceType = if (selectedPlaceType == type) {
                            null
                        } else {
                            type
                        }
                    }
                    PlaceType.values().forEach {
                        val typeUI = it.toUI()
                        val isSelected = selectedPlaceType == it
                        SelectableChip(
                            label = stringResource(typeUI.label),
                            icon = typeUI.icon,
                            shape = CircleShape,
                            selected = isSelected,
                            onClick = { onTypeClick(it) },
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(top = Spacing_05, start = Spacing_05),
                    text = "Etiquetas"
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = Spacing_05),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_03)
                ) {
                    PlaceTag.values().forEach {
                        val tagUI = it.toUI()
                        val selected = selectedPlaceTags.contains(it)
                        SelectableChip(
                            label = stringResource(tagUI.label),
                            icon = tagUI.icon,
                            selected = selected,
                            onClick = { onTagClick(it) },
                        )
                    }
                }
                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = Spacing_05),
                    onClick = { onFilterRequest(selectedPlaceType, selectedPlaceTags) },
                    content = { Text(text = "Filtrar resultados") }
                )
            }
        }
    }
}

@Composable
internal fun SortDialog(
    activeSorter: PlaceCardSorter,
    onDismissDialogRequest: () -> Unit,
    onSortRequest: (PlaceCardSorter) -> Unit
) {
    Dialog(onDismissRequest = onDismissDialogRequest) {
        Card {
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(Spacing_05),
                    text = "Ordenar por",
                    fontWeight = FontWeight.SemiBold,
                )
                var selectedSorter by remember { mutableStateOf(activeSorter) }
                PlaceCardSorter.values().forEach {
                    Row {
                        VURadioButton(
                            modifier = Modifier.fillMaxWidth(),
                            paddingValues = PaddingValues(Spacing_05),
                            label = stringResource(it.label),
                            selected = selectedSorter == it,
                            onClick = { selectedSorter = it },
                        )
                    }
                }
                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = Spacing_05),
                    onClick = { onSortRequest(selectedSorter) },
                    content = { Text(text = "Ordenar resultados") }
                )
            }
        }
    }
}
