@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel.Action
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel.UiState
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(
    navigateUp: () -> Unit,
    viewModel: ProductListViewModel = koinViewModel(),
) {

    ProductListScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )

    VUCircularProgressIndicator(visible = viewModel.uiState.loading)

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = navigateUp
    )
}

@Composable
private fun ProductListScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Column {
        VUTopAppBar(
            title = stringResource(uiState.category.label),
            onBackClick = { onAction(Action.OnBackClick) },
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {

        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateUp -> {
                    navigateUp()
                }
            }
        }.collect()
    }
}