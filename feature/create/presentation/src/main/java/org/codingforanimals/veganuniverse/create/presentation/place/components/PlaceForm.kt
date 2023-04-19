@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.create.presentation.place.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05

@Composable
internal fun PlaceForm(
    name: String,
    onNameChange: (String) -> Unit,
    openingHours: String,
    onOpeningHoursChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_05),
    ) {
        Text(
            modifier = Modifier.padding(
                start = Spacing_05,
                end = Spacing_05,
                top = Spacing_04,
            ),
            text = "Contanos sobre el lugar",
            fontWeight = FontWeight.SemiBold,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_05),
            value = name,
            onValueChange = { if (it.length < 64) onNameChange(it) },
            label = { Text("Nombre") },
            colors = VUTextFieldDefaults.colors(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing_05),
            value = openingHours,
            onValueChange = { if (it.length < 24) onOpeningHoursChange(it) },
            label = { Text("Horario de atención") },
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
                .padding(
                    start = Spacing_05,
                    end = Spacing_05,
                    bottom = Spacing_05,
                )
                .heightIn(min = 200.dp),
            value = description,
            onValueChange = { if (it.length < 255) onDescriptionChange(it) },
            label = { Text("Descripción") },
            colors = VUTextFieldDefaults.colors(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
    }
}