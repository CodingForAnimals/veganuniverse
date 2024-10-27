@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toUI

@Composable
fun AllCategories(
    modifier: Modifier = Modifier,
    onShowMoreClick: () -> Unit = {},
    onItemClick: (ProductCategory) -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.categories),
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = onShowMoreClick) {
                Text(
                    text = stringResource(R.string.show_more),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(
                Spacing_06, Alignment.CenterHorizontally
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            ProductCategory.entries.forEach { category ->
                key(category.name) {
                    val categoryUI = category.toUI()
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        onClick = { onItemClick(category) },
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentScale = ContentScale.Crop,
                            model = categoryUI.imageRef,
                            contentDescription = stringResource(categoryUI.label),
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(Spacing_03),
                            text = stringResource(categoryUI.label),
                            maxLines = 2,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            if (ProductCategory.entries.size % 2 == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}