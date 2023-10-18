@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.places.presentation.home.composables

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
import org.codingforanimals.veganuniverse.core.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.core.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05

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
                        val isSelected = selectedPlaceType == it
                        SelectableChip(
                            label = it.label,
                            icon = it.icon,
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
                        val selected = selectedPlaceTags.contains(it)
                        SelectableChip(
                            label = stringResource(it.label),
                            icon = it.icon,
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
    activeSorter: PlaceSorter,
    onDismissDialogRequest: () -> Unit,
    onSortRequest: (PlaceSorter) -> Unit
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
                PlaceSorter.values().forEach {
                    Row {
                        val label = when (it) {
                            PlaceSorter.NAME -> "Nombre"
                            PlaceSorter.DATE -> "Más recientes"
                            PlaceSorter.RATING -> "Mejor puntaje"
                            PlaceSorter.REVIEWS -> "Cantidad de reseñas"
                        }
                        VURadioButton(
                            modifier = Modifier.fillMaxWidth(),
                            paddingValues = PaddingValues(Spacing_05),
                            label = label,
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
