package org.codingforanimals.veganuniverse.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04

@Composable
fun ContentSubtitle(
    modifier: Modifier = Modifier,
    label: Int,
    buttonLabel: Int? = null,
    onButtonClick: () -> Unit = {},
    leadingIcon: Icon? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        leadingIcon?.let { VUIcon(icon = it, contentDescription = stringResource(label)) }
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        buttonLabel?.let {
            TextButton(onClick = onButtonClick) {
                Text(text = stringResource(buttonLabel))
            }
        }
    }
}