@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.profile.itemlist.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.places.ui.PlaceCard
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListViewModel.Action
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListViewModel.SideEffect
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListViewModel.UiState
import org.codingforanimals.veganuniverse.ui.R
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.Spacing_07
import org.codingforanimals.veganuniverse.ui.cards.LoadingSimpleCard
import org.codingforanimals.veganuniverse.ui.cards.SimpleCard
import org.codingforanimals.veganuniverse.ui.cards.SimpleCardItem
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileItemListScreen(
    navigateUp: () -> Unit,
    navigateToRecipe: (String) -> Unit,
    navigateToPlace: (String) -> Unit,
    viewModel: ProfileItemListViewModel = koinViewModel(),
) {
    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = navigateUp,
        navigateToRecipe = navigateToRecipe,
        navigateToPlace = navigateToPlace,
    )

    ProfileItemListScreen(
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )

}

@Composable
private fun ProfileItemListScreen(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        VUTopAppBar(
            title = uiState.title?.let { stringResource(id = it) } ?: "",
            onBackClick = { onAction(Action.OnBackClick) }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing_07),
            contentPadding = PaddingValues(Spacing_06)
        ) {
            itemsIndexed(uiState.items) { index, item ->
                key(index) {
                    fun onItemClick(id: String) {
                        onAction(Action.OnItemClick(id))
                    }
                    when (item) {
                        is SimpleCardItem -> SimpleCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(2f),
                            model = item,
                            onClick = ::onItemClick
                        )

                        is PlaceCardItem -> PlaceCard(placeCard = item, onCardClick = ::onItemClick)
                    }
                }
            }
            if (uiState.loading) {
                items(2) {
                    key(it) {
                        AnimatedVisibility(visible = true) {
                            LoadingSimpleCard()
                        }
                    }
                }
            }
            item {
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing_06),
                    visible = uiState.canLoadMore && !uiState.loading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    TextButton(onClick = { onAction(Action.OnLoadMoreClick) }) {
                        Text(text = stringResource(id = R.string.load_more))
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    navigateToRecipe: (String) -> Unit,
    navigateToPlace: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToPlace -> {
                    navigateToPlace(sideEffect.geoHash)
                }

                is SideEffect.NavigateToRecipe -> {
                    navigateToRecipe(sideEffect.id)
                }

                SideEffect.NavigateUp -> {
                    navigateUp()
                }
            }
        }.collect()
    }
}

