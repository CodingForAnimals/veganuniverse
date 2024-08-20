package org.codingforanimals.veganuniverse.registration.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08

@Composable
internal fun SecondaryAuthOptionDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spacing_08, bottom = Spacing_04),
        horizontalArrangement = Arrangement.spacedBy(Spacing_06),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                .weight(1f)
        )
        Text(text = "ó bien")
        Spacer(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                .weight(1f)
        )
    }
}