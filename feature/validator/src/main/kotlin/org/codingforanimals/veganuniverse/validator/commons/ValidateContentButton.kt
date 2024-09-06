package org.codingforanimals.veganuniverse.validator.commons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.validator.R

@Composable
internal fun ValidateContentButton(
    onValidate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = modifier,
        onClick = onValidate
    ) {
        Text(stringResource(R.string.validate))
    }
}