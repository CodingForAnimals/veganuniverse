package org.codingforanimals.places.presentation.details.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.R
import org.codingforanimals.veganuniverse.core.ui.components.VUImage
import org.codingforanimals.veganuniverse.core.ui.icons.VUImages
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun ErrorView(
    @StringRes message: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VUImage(
            modifier = Modifier
                .size(200.dp)
                .padding(top = Spacing_06),
            image = VUImages.ErrorCat,
        )
        Text(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(bottom = Spacing_06),
            textAlign = TextAlign.Center,
            text = stringResource(message)
        )
    }
}

@Composable
internal fun ErrorDialog(
    title: String,
    message: String,
    onConfirmButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(stringResource(R.string.error_dismiss_button_label))
            }
        },
    )
}