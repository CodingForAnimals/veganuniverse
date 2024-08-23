@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.toUI
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.cards.VUCardDefaults

@Composable
fun AllCategories(
    categories: List<ProductCategory>,
    onShowMoreClick: () -> Unit,
    onItemClick: (ProductCategory) -> Unit,
) {
    Row(
        modifier = Modifier.padding(bottom = Spacing_02),
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
        categories.forEach { category ->
            key(category.name) {
                val categoryUI = category.toUI()
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onItemClick(category) },
                    elevation = VUCardDefaults.elevatedCardElevation(),
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
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}