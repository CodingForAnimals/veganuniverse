package org.codingforanimals.veganuniverse.create.presentation.place.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.core.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

@Composable
internal fun SelectPlaceType(
    selectedPlaceType: CreatePlaceViewModel.PlaceType?,
    onButtonClick: (CreatePlaceViewModel.PlaceType) -> Unit,
) {
    Card(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(horizontal = Spacing_05),
    ) {
        Text(
            modifier = Modifier.padding(start = Spacing_05, end = Spacing_05, top = Spacing_04),
            text = "Tipo de lugar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        CreatePlaceViewModel.PlaceType.values().forEachIndexed { index, placeType ->
            VURadioButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onButtonClick(placeType) },
                selected = selectedPlaceType?.name == placeType.name,
                label = placeType.label,
                icon = placeType.icon,
                paddingValues = PaddingValues(horizontal = Spacing_06),
            )
        }
    }
}