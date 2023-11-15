package org.codingforanimals.veganuniverse.create.place.presentation.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.place.presentation.R
import org.codingforanimals.veganuniverse.create.place.presentation.model.TypeField
import org.codingforanimals.veganuniverse.places.ui.PlaceType
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.Spacing_07
import org.codingforanimals.veganuniverse.ui.components.VURadioButton

@Composable
internal fun IconSelector(
    typeField: TypeField,
    isValidating: Boolean,
    onAction: (CreatePlaceViewModel.Action) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_06),
        text = stringResource(R.string.place_icon_select_title),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    val colors = if (isValidating && !typeField.isValid) {
        RadioButtonDefaults.colors(unselectedColor = MaterialTheme.colorScheme.error)
    } else {
        RadioButtonDefaults.colors()
    }

    for (type in PlaceType.values()) {
        val isSelected = remember(typeField) { typeField.value == type }
        VURadioButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onAction(CreatePlaceViewModel.Action.OnFormChange(type = type)) },
            selected = isSelected,
            label = type.label,
            icon = type.icon,
            paddingValues = PaddingValues(horizontal = Spacing_07),
            colors = colors,
        )
    }
}