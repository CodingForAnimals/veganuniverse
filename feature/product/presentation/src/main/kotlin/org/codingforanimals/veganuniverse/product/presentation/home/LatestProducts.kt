package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.animation.shimmer
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductRow
import org.codingforanimals.veganuniverse.product.presentation.components.ProductRowLoading

@Composable
fun LatestProducts(
    latestProductsState: ProductHomeViewModel.LatestProductsState,
    onShowMoreClick: () -> Unit,
    onImageClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onReportClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(bottom = Spacing_02),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.all_products),
            style = MaterialTheme.typography.titleMedium,
        )
        TextButton(onClick = onShowMoreClick) {
            Text(
                text = stringResource(R.string.show_more),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
    Crossfade(
        modifier = Modifier.animateContentSize(),
        targetState = latestProductsState,
        label = "products_home_latest_products_crossfade",
        content = { state ->
            when (state) {
                ProductHomeViewModel.LatestProductsState.Error -> ErrorView(message = unexpected_error_message)
                ProductHomeViewModel.LatestProductsState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shimmer()
                    ) {
                        ProductRowLoading()
                        ProductRowLoading()
                        ProductRowLoading()
                    }
                }

                is ProductHomeViewModel.LatestProductsState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(Spacing_05),
                            )
                            .clip(RoundedCornerShape(Spacing_05)),
                    ) {
                        state.products.forEach { product ->
                            key(product.id) {
                                ProductRow(
                                    product = product,
                                    onImageClick = {
                                        product.imageUrl?.let {
                                            onImageClick(product.imageUrl)
                                        }
                                    },
                                    onEditClick = {
                                        product.id?.let {
                                            onEditClick(it)
                                        }
                                    },
                                    onReportClick = {
                                        product.id?.let {
                                            onReportClick(it)
                                        }
                                    },
                                )
                            }
                        }
                    }
                }

            }
        })
}