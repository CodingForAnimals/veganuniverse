@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.listing

import android.util.Log
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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.details.composables.ErrorView
import org.codingforanimals.veganuniverse.place.presentation.home.bottomsheet.PlaceCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PlaceListingScreen(
    listingType: String?,
    navigateUp: () -> Unit,
    navigateToPlaceDetails: (String) -> Unit,
) {
    val viewModel: PlaceListingViewModel = koinViewModel(
        parameters = { parametersOf(listingType) }
    )

    val places = viewModel.places.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    viewModel.title?.let {
                        Text(text = stringResource(id = it))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(id = back),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = Spacing_05,
                start = Spacing_05,
                end = Spacing_05,
                bottom = Spacing_08,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing_05),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(places.itemCount) { index ->
                key(index) {
                    val place = places[index] ?: return@items
                    PlaceCard(
                        modifier = Modifier.animateAlphaOnStart(),
                        placeCard = place,
                        onCardClick = navigateToPlaceDetails,
                    )
                }
            }

            places.loadState.apply {
                when {
                    refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                    append is LoadState.Loading -> item { CircularProgressIndicator() }
                    places.itemCount == 0 -> item { ErrorView(message = R.string.empty_places_message) }
                }
            }
        }
    }

}


