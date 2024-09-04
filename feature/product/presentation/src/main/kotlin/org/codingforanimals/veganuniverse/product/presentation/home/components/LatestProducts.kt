package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.R.string.most_recent
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.commons.ui.animation.shimmer
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductCard
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel
import java.util.Date

@Composable
internal fun LatestProductsCards(
    latestProductsState: ProductHomeViewModel.LatestProductsState,
    modifier: Modifier = Modifier,
    onShowMoreClick: () -> Unit = {},
    onProductClick: (Product) -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(most_recent),
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
                    ProductHomeViewModel.LatestProductsState.Loading -> LatestProductsLoadingCards()
                    is ProductHomeViewModel.LatestProductsState.Success -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing_05)
                        ) {
                            state.products.forEach { product ->
                                key(product.id) {
                                    ProductCard(
                                        product = product,
                                        onClick = { onProductClick(product) },
                                    )
                                }
                            }
                        }
                    }

                }
            }
        )
    }
}

@Composable
private fun LatestProductsLoadingCards() {
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

@Composable
private fun ProductRowLoading() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerItem(
            modifier = Modifier
                .padding(
                    start = Spacing_06,
                    top = Spacing_05,
                    end = Spacing_05,
                    bottom = Spacing_05
                )
                .size(68.dp)
                .clip(ShapeDefaults.Medium)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing_03)
        ) {
            ShimmerItem(
                Modifier
                    .fillMaxWidth(0.5f)
                    .height(20.dp)
                    .clip(ShapeDefaults.Small)
            )
            ShimmerItem(
                Modifier
                    .fillMaxWidth(0.3f)
                    .height(20.dp)
                    .clip(ShapeDefaults.Small)
            )
        }
    }
    HorizontalDivider()
}

@Preview
@Composable
private fun PreviewLatestProductsCards() {
    VeganUniverseTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(Spacing_05)
            ) {
                val product = Product(
                    id = "123",
                    name = "Producto Pepe",
                    brand = "Argento's",
                    comment = "Rico y económico. 100% vegano. Recomiendo!!",
                    type = ProductType.VEGAN,
                    category = ProductCategory.ADDITIVES,
                    userId = "123123",
                    username = "Paola Argento",
                    imageUrl = null,
                    createdAt = Date(),
                    validated = true,
                )
                LatestProductsCards(
                    latestProductsState = ProductHomeViewModel.LatestProductsState.Success(
                        listOf(
                            product,
                            product,
                            product
                        )
                    ),
                )
            }
        }
    }
}
