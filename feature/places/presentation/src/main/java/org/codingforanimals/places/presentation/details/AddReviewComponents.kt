@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.places.presentation.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun AddReviewPost(
    rating: Int,
    showDiscardReviewDialog: () -> Unit,
    submitReview: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val headerData = HeaderData(
        imageRes = R.drawable.vegan_restaurant,
        title = {
            Text(text = "Mi usuario")
            RatingBar(rating = rating)
        },
        actions = {
            VUIcon(
                icon = VUIcons.Delete,
                contentDescription = "",
                onIconClick = showDiscardReviewDialog,
            )
        }
    )
    GenericPost(
        modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
        headerData = headerData,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(),
        content = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_05),
                value = title,
                onValueChange = { },
                label = { Text("¿Cómo fue tu experiencia?") },
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
                    .heightIn(min = 200.dp)
                    .padding(horizontal = Spacing_05),
                value = description,
                onValueChange = { if (it.length < 255) description = it },
                label = { Text("Contanos con más detalle") },
                colors = VUTextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        },
        actions = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06, vertical = Spacing_04),
                colors = ButtonDefaults.buttonColors(),
                onClick = submitReview,
                enabled = (title.isNotEmpty() && description.isNotEmpty()),
            ) {
                Text(text = "Publicar reseña")
            }
        }
    )
}

@Composable
internal fun DiscardReviewDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = { Text(text = "¿Quieres descartar esta reseña?") },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Descartar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Volver")
            }
        },
        icon = { VUIcon(icon = VUIcons.Delete, contentDescription = "") },
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.onSurface,
    )
}