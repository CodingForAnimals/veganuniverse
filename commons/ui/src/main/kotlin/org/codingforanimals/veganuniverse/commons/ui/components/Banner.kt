package org.codingforanimals.veganuniverse.commons.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.PrimaryUltraLight
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun Banner(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    icon: Icon = VUIcons.ChevronRight
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, CardDefaults.shape),
        onClick = { onClick?.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = PrimaryUltraLight,
        ),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Spacing_06,
                vertical = Spacing_05,
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.titleMedium,
            )
            onClick?.let {
                VUIcon(
                    modifier = Modifier
                        .padding(start = Spacing_05)
                        .size(28.dp),
                    icon = icon,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBanner() {
    VeganUniverseTheme {
        Column(
            modifier = Modifier.padding(Spacing_06),
            verticalArrangement = Arrangement.spacedBy(Spacing_06)
        ) {
            Banner(
                "Banner 1"
            )
            Banner(
                text = "Banner 2",
                onClick = {}
            )
        }
    }
}