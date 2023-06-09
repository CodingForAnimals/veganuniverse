package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.core.ui.components.VURadioButton
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_07

@Composable
internal fun SelectPlaceType(
    type: PlaceType?,
    typeError: Boolean,
    onButtonClick: (PlaceType) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_06),
        text = "Cuál te parece la categoría más acertada para este lugar?",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )

    val colors = if (typeError) {
        RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.error)
    } else RadioButtonDefaults.colors()
    PlaceType.values().forEach { placeType ->
        VURadioButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onButtonClick(placeType) },
            selected = type?.name == placeType.name,
            label = placeType.label,
            icon = placeType.icon,
            paddingValues = PaddingValues(horizontal = Spacing_07),
            colors = colors,
        )
    }
}