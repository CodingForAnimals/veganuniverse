@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.core.ui.components.VUSelectableChip
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05

@Composable
internal fun SelectTags(
    selectedTags: List<PlaceTag>,
    onTagClick: (PlaceTag) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_05),
        text = "Etiquetas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Spacing_05, end = Spacing_05, top = Spacing_03),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        PlaceTag.values().forEach {
            val selected = selectedTags.contains(it)
            VUSelectableChip(
                label = it.label,
                icon = it.icon,
                selected = selected,
                onClick = { onTagClick(it) },
            )
        }
    }
}