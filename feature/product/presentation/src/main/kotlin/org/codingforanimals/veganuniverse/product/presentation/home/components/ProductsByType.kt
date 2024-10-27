@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toUI

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
private fun PreviewProductsByType(
) {
    VeganUniverseTheme {
        Surface {
            ProductsByType(modifier = Modifier.padding(Spacing_05))
        }
    }
}

@Composable
internal fun FilterProductsByType(
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
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing_06),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
            maxItemsInEachRow = 2,
        ) {
            ProductType.entries.forEach {
                key(it.name) {
                    val ui = remember { it.toUI() }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 3.dp,
                                color = ui.color.copy(alpha = 0.5f),
                                shape = ShapeDefaults.Large
                            )
                            .shadow(
                                elevation = 3.dp,
                                shape = ShapeDefaults.Large
                            ),
                        onClick = { onTypeClick(it) },
                        shape = ShapeDefaults.Large
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(Spacing_04)
                                .wrapContentHeight(Alignment.CenterVertically),
                            verticalArrangement = Arrangement.spacedBy(
                                space = Spacing_03,
                                alignment = Alignment.CenterVertically,
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f),
                                painter = painterResource(ui.icon.id),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                text = ui.shortLabel,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
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
private fun PreviewProduct() {
    VeganUniverseTheme {
        Surface {
            FilterProductsByType(
                Modifier.padding(Spacing_06)
            )
        }
    }
}
