@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.validate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_01
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.snackbar.LocalSnackbarSender
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.home.composables.PlaceCard
import org.codingforanimals.veganuniverse.place.presentation.model.toCard
import org.codingforanimals.veganuniverse.place.shared.model.Place
import org.koin.androidx.compose.koinViewModel

@Composable
fun ValidatePlacesScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: ValidatePlacesViewModel = koinViewModel()
    val places = viewModel.unvalidatedPlaces.collectAsLazyPagingItems()
    var selectedPlaceForValidation by remember { mutableStateOf<Place?>(null) }

    Scaffold(
        topBar = {
            VUTopAppBar(
                onBackClick = onBackClick,
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
            items(places.itemCount) { index ->
                val place = places[index] ?: return@items
                key(place.geoHash) {
                    val placeCardUI = remember { place.toCard() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing_01),
                    ) {
                        PlaceCard(
                            placeCard = placeCardUI,
                            onCardClick = { viewModel.onPlaceClick(place) },
                        )
                        TextButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = { selectedPlaceForValidation = place }
                        ) {
                            Text(stringResource(R.string.validate))
                        }
                    }
                }
            }

            places.loadState.apply {
                when {
                    refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                    append is LoadState.Loading -> item { CircularProgressIndicator() }
                }
            }
        }
    }

    selectedPlaceForValidation?.let {
        AlertDialog(
            onDismissRequest = { selectedPlaceForValidation = null },
            title = {
                Text(stringResource(R.string.validate_content_title))
            },
            text = {
                Text(stringResource(R.string.validate_content_text))
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPlaceForValidation = null
                        viewModel.validatePlace(it)
                    },
                    content = {
                        Text(stringResource(R.string.confirm))
                    }
                )
            }
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
                ValidatePlacesViewModel.SideEffect.Refresh -> {
                    places.refresh()
                }
            }
        }.collect()
    }
}
