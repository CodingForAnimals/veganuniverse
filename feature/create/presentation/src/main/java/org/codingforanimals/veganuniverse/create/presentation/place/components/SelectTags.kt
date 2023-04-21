@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.presentation.place.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.core.ui.components.VUSelectableChip
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_10
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

@Composable
internal fun SelectTags() {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_05),
        text = "Etiquetas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Spacing_05, end = Spacing_05, top = Spacing_03, bottom = Spacing_10),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        CreatePlaceViewModel.PlaceTag.values().forEach {
            var selected by remember { mutableStateOf(false) }
            VUSelectableChip(
                label = it.label,
                icon = it.icon,
                selected = selected,
                onClick = { selected = !selected },
            )
        }
    }
}