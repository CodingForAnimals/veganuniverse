@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.validate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_01
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.R.string.delete
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.component.ProductCard
import org.codingforanimals.veganuniverse.product.presentation.component.ProductEditCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun ValidateProductsScreen(
    navigateToAdditiveEdits: () -> Unit,
    onBackClick: () -> Unit,
    navigateToProductDetail: (String) -> Unit,
    navigateToCompareProductEdit: (String, String) -> Unit,
) {
    val viewModel: ValidateProductsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var selectedProductForValidation by remember { mutableStateOf<Product?>(null) }
    var selectedProductForElimination by remember { mutableStateOf<Product?>(null) }
    var selectedProductEditForValidation by remember { mutableStateOf<ProductEdit?>(null) }
    var selectedProductEditForElimination by remember { mutableStateOf<ProductEdit?>(null) }
    var showValidateAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VUTopAppBar(
                onBackClick = onBackClick,
                actions = {
                    TextButton(navigateToAdditiveEdits) {
                        Text(stringResource(R.string.additives))
                    }
                    if (state is ValidateProductsViewModel.State.Success) {
                        TextButton(onClick = { showValidateAllDialog = true }) {
                            Text(stringResource(R.string.validate_all))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val currentState = state) {
            ValidateProductsViewModel.State.Error -> {
                LaunchedEffect(Unit) {

                }
            }

            ValidateProductsViewModel.State.Loading -> {
                VUCircularProgressIndicator()
            }

            is ValidateProductsViewModel.State.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(Spacing_06),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(Spacing_06)
                ) {
                    itemsIndexed(currentState.unvalidated) { index, product ->
                        key(product.hashCode()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(Spacing_01),
                            ) {
                                ProductCard(
                                    product = product,
                                    onClick = {
                                        product.id?.let {
                                            navigateToProductDetail(it)
                                        }
                                    },
                                )
                                Row(
                                    modifier = Modifier.align(Alignment.End),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing_01)
                                ) {
                                    TextButton(
                                        onClick = { selectedProductForValidation = product }
                                    ) {
                                        Text(stringResource(R.string.validate))
                                    }
                                    TextButton(
                                        onClick = { selectedProductForElimination = product }
                                    ) {
                                        Text(stringResource(delete))
                                    }
                                }
                            }
                        }
                    }
                    itemsIndexed(currentState.edits) { index, edit ->
                        key(edit.hashCode()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(Spacing_01),
                            ) {
                                ProductEditCard(
                                    product = edit,
                                    onClick = {
                                        edit.id?.let {
                                            navigateToCompareProductEdit(it, edit.originalId)
                                        }
                                    },
                                )
                                Row(
                                    modifier = Modifier.align(Alignment.End),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing_01)
                                ) {
                                    TextButton(
                                        onClick = { selectedProductEditForValidation = edit }
                                    ) {
                                        Text(stringResource(R.string.validate))
                                    }
                                    TextButton(
                                        onClick = { selectedProductEditForElimination = edit }
                                    ) {
                                        Text(stringResource(delete))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedProductForValidation?.let {
        AlertDialog(
            onDismissRequest = { selectedProductForValidation = null },
            title = {
                Text(stringResource(R.string.validate_content_title))
            },
            text = {
                Text(stringResource(R.string.validate_content_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedProductForValidation = null
                        viewModel.onConfirmValidateProduct(it)
                    },
                    content = {
                        Text(stringResource(R.string.confirm))
                    }
                )
            }
        )
    }
    selectedProductEditForValidation?.let {
        AlertDialog(
            onDismissRequest = { selectedProductEditForValidation = null },
            title = {
                Text(stringResource(R.string.validate_content_title))
            },
            text = {
                Text(stringResource(R.string.validate_content_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedProductEditForValidation = null
                        viewModel.onConfirmValidateProductEdit(it)
                    },
                    content = {
                        Text(stringResource(R.string.confirm))
                    }
                )
            }
        )
    }

    selectedProductForElimination?.let {
        AlertDialog(
            onDismissRequest = { selectedProductForElimination = null },
            title = {
                Text(stringResource(R.string.eliminate_edit_title))
            },
            text = {
                Text(stringResource(R.string.eliminate_edit_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedProductForElimination = null
                        viewModel.onConfirmDeleteProduct(it)
                    },
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        )
    }

    selectedProductEditForElimination?.let {
        AlertDialog(
            onDismissRequest = { selectedProductEditForElimination = null },
            title = {
                Text(stringResource(R.string.eliminate_edit_title))
            },
            text = {
                Text(stringResource(R.string.eliminate_edit_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedProductEditForElimination = null
                        viewModel.onConfirmDeleteEdit(it)
                    },
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        )
    }

    if (showValidateAllDialog) {
        AlertDialog(
            onDismissRequest = { showValidateAllDialog = false },
            title = {
                Text(stringResource(R.string.validate_all_content_title))
            },
            text = {
                Text(stringResource(R.string.validate_all_content_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showValidateAllDialog = false
                        viewModel.onConfirmValidateAll()
                    },
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        )
    }

    val displaySnackbar = LocalSnackbarSender.current
    LaunchedEffect(Unit) {
        viewModel.snackbarEffects.onEach { snackbar ->
            displaySnackbar(snackbar)
        }.collect()
    }
}
