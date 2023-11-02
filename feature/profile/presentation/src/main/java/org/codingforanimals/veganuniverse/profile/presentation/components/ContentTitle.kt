package org.codingforanimals.veganuniverse.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04

@Composable
internal fun ContentTitle(
    modifier: Modifier = Modifier,
    icon: Icon,
    label: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        VUIcon(
            icon = icon,
            contentDescription = stringResource(label)
        )
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleLarge
        )
    }
}