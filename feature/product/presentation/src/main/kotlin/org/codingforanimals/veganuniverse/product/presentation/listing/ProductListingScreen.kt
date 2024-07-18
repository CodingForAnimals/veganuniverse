package org.codingforanimals.veganuniverse.product.presentation.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductCard
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun ProductListingScreen(
    navigateUp: () -> Unit,
    onProductClick: (String) -> Unit,
) {
    val viewModel: ProductListingViewModel = koinViewModel()
    val products = viewModel.products.collectAsLazyPagingItems()
    ProductListingScreen(
        title = viewModel.title,
        emptyResultLabel = viewModel.emptyResultsTextRes,
        products = products,
        navigateUp = navigateUp,
        onProductClick = onProductClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListingScreen(
    title: Int,
    emptyResultLabel: Int,
    products: LazyPagingItems<Product>,
    navigateUp: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = stringResource(id = title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                        content = {
                            Icon(
                                imageVector = VUIcons.ArrowBack.imageVector,
                                contentDescription = stringResource(id = back)
                            )
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(Spacing_05),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = Spacing_05,
                end = Spacing_05,
                top = Spacing_05,
                bottom = Spacing_08,
            ),
        ) {
            items(products.itemCount) { index ->
                val product = products[index]
                val id = product?.id ?: return@items
                ProductCard(
                    product = product,
                    onClick = { onProductClick(id) }
                )
            }

            products.loadState.apply {
                when {
                    refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                    append is LoadState.Loading -> item { CircularProgressIndicator() }
                    products.itemCount == 0 -> item { ErrorView(message = emptyResultLabel) }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewEmptyProductListingScreen() {
    VeganUniverseTheme {
        val products = flowOf(PagingData.empty<Product>()).collectAsLazyPagingItems()
        ProductListingScreen(
            title = R.string.bookmarked_products,
            emptyResultLabel = R.string.empty_bookmarked_products_message,
            products = products,
        )
    }
}

@Preview
@Composable
private fun PreviewProductListingScreen() {
    VeganUniverseTheme {
        val productA = Product(
            id = "123",
            name = "Galletita Pepe",
            brand = "Argento's",
            comment = "Excelente galletita, económica y 100% vegana. Recomiendo!",
            type = ProductType.VEGAN.toUI(),
            category = ProductCategory.BAKED_GOODS.toUI(),
            userId = "123",
            username = "El Pepe",
            imageUrl = null,
            createdAt = Date()
        )
        val productB = productA.copy(id = "1234")
        val products = flowOf(PagingData.from(listOf(productA, productB))).collectAsLazyPagingItems()
        ProductListingScreen(
            title = R.string.bookmarked_products,
            emptyResultLabel = R.string.empty_bookmarked_products_message,
            products = products,
        )
    }
}
