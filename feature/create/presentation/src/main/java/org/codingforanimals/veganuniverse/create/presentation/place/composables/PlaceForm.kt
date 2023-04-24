@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel

@Composable
internal fun PlaceForm(
    form: CreatePlaceViewModel.Form,
    onTypeSelect: (PlaceType) -> Unit,
    onNameChange: (String) -> Unit,
    onOpeningHoursChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    SelectPlaceType(
        type = form.type,
        typeError = form.typeError,
        onButtonClick = onTypeSelect,
    )

    FormTextFields(
        name = form.name,
        nameError = form.nameError,
        onNameChange = onNameChange,
        openingHours = form.openingHours,
        openingHoursError = form.openingHoursError,
        onOpeningHoursChange = onOpeningHoursChange,
        description = form.description,
        descriptionError = form.descriptionError,
        onDescriptionChange = onDescriptionChange,
    )

}

@Composable
private fun FormTextFields(
    name: String,
    nameError: Boolean,
    onNameChange: (String) -> Unit,
    openingHours: String,
    openingHoursError: Boolean,
    onOpeningHoursChange: (String) -> Unit,
    description: String,
    descriptionError: Boolean,
    onDescriptionChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06, vertical = Spacing_02),
        value = name,
        isError = nameError,
        onValueChange = { if (it.length < 64) onNameChange(it) },
        placeholder = { Text("Nombre") },
        colors = VUTextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next,
        ),
    )
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06, vertical = Spacing_02)
            .heightIn(min = 200.dp),
        value = description,
        isError = descriptionError,
        onValueChange = { if (it.length < 255) onDescriptionChange(it) },
        placeholder = { Text("Descripción") },
        colors = VUTextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06, vertical = Spacing_02),
        value = openingHours,
        isError = openingHoursError,
        onValueChange = { if (it.length < 24) onOpeningHoursChange(it) },
        placeholder = { Text("Horario de atención") },
        leadingIcon = { VUIcon(icon = VUIcons.Clock, contentDescription = "") },
        colors = VUTextFieldDefaults.colors(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next,
        ),
    )
}