@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toUI

@Composable
internal fun SelectCategoryScreen(
    onCategoryClick: (ProductCategory) -> Unit,
    currentCategory: ProductCategory?,
    modifier: Modifier = Modifier,
) {
    Surface(modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(Spacing_06),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            Text(
                modifier = Modifier.padding(top = Spacing_06),
                text = stringResource(R.string.product_category),
                style = MaterialTheme.typography.titleMedium,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing_06),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing_06,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                ProductCategory.entries.forEach { category ->
                    key(category.name) {
                        val categoryUI = remember { category.toUI() }
                        val isSelected = remember(currentCategory) { category == currentCategory }
                        val border = if (isSelected) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onCategoryClick(category) },
                            border = border
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
}
