package org.codingforanimals.veganuniverse.validator.product.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_01
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.product.presentation.component.ProductCard
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.codingforanimals.veganuniverse.validator.R
import org.codingforanimals.veganuniverse.validator.commons.ValidateContentAlertDialog
import org.codingforanimals.veganuniverse.validator.commons.ValidateContentButton
import org.codingforanimals.veganuniverse.validator.navigation.ValidatorTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ValidateProductsScreen(
    navigateToAdditiveEdits: () -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: ValidateProductsViewModel = koinViewModel()
    val products = viewModel.unvalidatedProducts.collectAsLazyPagingItems()

    var selectedProductForValidation by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            ValidatorTopAppBar(
                onBackClick = onBackClick,
                actions = {
                    TextButton(navigateToAdditiveEdits) {
                        Text(stringResource(R.string.additives))
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(Spacing_06)
        ) {
            items(products.itemCount) { index ->
                val product = products[index] ?: return@items
                key(product.id) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing_01),
                    ) {
                        ProductCard(
                            product = product,
                            onClick = { viewModel.onProductClick(product) },
                        )
                        ValidateContentButton(
                            modifier = Modifier.align(Alignment.End),
                            onValidate = { selectedProductForValidation = product }
                        )
                    }
                }
            }

            products.loadState.apply {
                when {
                    refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                    append is LoadState.Loading -> item { CircularProgressIndicator() }
                }
            }
        }
    }
    selectedProductForValidation?.let {
        ValidateContentAlertDialog(
            onDismissRequest = { selectedProductForValidation = null },
            onConfirm = { viewModel.validateProduct(it) }
        )
    }

    val displaySnackbar = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.snackbarEffects.onEach { snackbar ->
            displaySnackbar(snackbar)
        }.collect()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.onEach { effect ->
            when (effect) {
                ValidateProductsViewModel.SideEffect.Refresh -> {
                    products.refresh()
                }
            }
        }.collect()
    }
}
