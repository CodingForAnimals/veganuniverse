package org.codingforanimals.veganuniverse.shared.ui.grid

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.Deferred
import org.codingforanimals.veganuniverse.core.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.core.ui.animation.shimmer
import org.codingforanimals.veganuniverse.core.ui.error.ErrorView
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.shared.ui.grid.model.ContainerLayoutType
import org.codingforanimals.veganuniverse.shared.ui.grid.model.SimpleCardLayoutType
import org.codingforanimals.veganuniverse.shared.ui.grid.model.StaggeredItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StaggeredItemsGrid(
    items: Deferred<List<StaggeredItem>>,
    layoutType: ContainerLayoutType,
    onClick: (String) -> Unit,
    viewModel: StaggeredItemsGridViewModel = koinViewModel(parameters = { parametersOf(items) }),
) {
    val state = viewModel.items.collectAsState()
    Crossfade(targetState = state.value, label = "staggered_items_grid_crossfade") { currentState ->
        when (currentState) {
            StaggeredItemsGridViewModel.UiState.Error -> StaggeredItemsErrorView()
            StaggeredItemsGridViewModel.UiState.Loading -> StaggeredItemsLoadingGrid()
            is StaggeredItemsGridViewModel.UiState.Success -> StaggeredItemsSuccessGrid(
                items = currentState.items,
                layoutType = layoutType,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun StaggeredItemsErrorView() {
    ErrorView(message = org.codingforanimals.veganuniverse.core.ui.R.string.unknown_error_message)
}

@Composable
private fun StaggeredItemsLoadingGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shimmer(),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(ShapeDefaults.Small)
            )
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(ShapeDefaults.Small)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            ShimmerItem(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(ShapeDefaults.Small)
            )
        }
    }
}

@Composable
private fun StaggeredItemsSuccessGrid(
    items: List<StaggeredItem>,
    layoutType: ContainerLayoutType,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                ContainerLayoutType.VERTICAL_LEFT -> {
                    items.getOrNull(0)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Vertical,
                            onClick = { onClick(item.id) }
                        )
                    }
                }

                ContainerLayoutType.VERTICAL_RIGHT -> {
                    items.getOrNull(1)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) }
                        )
                    }
                    items.getOrNull(2)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) }
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                ContainerLayoutType.VERTICAL_LEFT -> {
                    items.getOrNull(1)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) }
                        )
                    }
                    items.getOrNull(2)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Squared,
                            onClick = { onClick(item.id) }
                        )
                    }
                }

                ContainerLayoutType.VERTICAL_RIGHT -> {
                    items.getOrNull(0)?.let { item ->
                        org.codingforanimals.veganuniverse.shared.ui.grid.components.SimpleCard(
                            title = item.title,
                            imageRef = item.imageRef,
                            layoutType = SimpleCardLayoutType.Vertical,
                            onClick = { onClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}
