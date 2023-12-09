@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.categories.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.product.categories.presentation.ProductCategoriesViewModel.Action
import org.codingforanimals.veganuniverse.product.categories.presentation.ProductCategoriesViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.categories.presentation.ProductCategoriesViewModel.UiState
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductCategoriesScreen(
    navigateToCategoryListScreen: (String) -> Unit,
    viewModel: ProductCategoriesViewModel = koinViewModel(),
) {

    ProductCategoriesScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToCategoryListScreen = navigateToCategoryListScreen,
    )
}

@Composable
private fun ProductCategoriesScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    LazyColumn {
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing_06,
                    Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                uiState.categories.forEach { category ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .shadow(10.dp, shape = CardDefaults.shape),
                        onClick = { onAction(Action.OnProductCategorySelected(category)) },
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentScale = ContentScale.Crop,
                            model = category.imageRef,
                            contentDescription = stringResource(category.label),
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(Spacing_03),
                            text = stringResource(category.label),
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
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToCategoryListScreen: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToCategoryListScreen -> {
                    navigateToCategoryListScreen(sideEffect.categoryName)
                }
            }
        }.collect()
    }
}
