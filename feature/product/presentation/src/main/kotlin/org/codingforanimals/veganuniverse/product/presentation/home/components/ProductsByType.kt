package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R

@Composable
internal fun ProductsByType(
    modifier: Modifier = Modifier,
    onShowAllClick: () -> Unit = {},
    onTypeClick: (ProductType) -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.categories_header_message),
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = onShowAllClick) {
                Text(
                    text = stringResource(R.string.show_all),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing_05)
        ) {
            ProductType.entries.forEach { type ->
                key(type.name) {
                    val typeUI = remember { type.toUI() }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTypeClick(type) },
                    ) {
                        Row(
                            modifier = Modifier.padding(Spacing_05),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Spacing_05),
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(id = typeUI.icon.id),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                            Text(
                                text = stringResource(typeUI.label),
                                style = MaterialTheme.typography.headlineSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewProductsByType() {
    VeganUniverseTheme {
        Surface {
            ProductsByType(modifier = Modifier.padding(Spacing_05))
        }
    }
}
