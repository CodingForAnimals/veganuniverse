@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.places.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.DetailsState
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.ReviewsState
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.UiState
import org.codingforanimals.places.presentation.details.composables.AddressAndOpeningHours
import org.codingforanimals.places.presentation.details.composables.DiscardReviewDialog
import org.codingforanimals.places.presentation.details.composables.ErrorDialog
import org.codingforanimals.places.presentation.details.composables.FlowRowTags
import org.codingforanimals.places.presentation.details.composables.PlaceDetailsTopAppBar
import org.codingforanimals.places.presentation.details.composables.Reviews
import org.codingforanimals.places.presentation.details.composables.StaticMap
import org.codingforanimals.places.presentation.details.composables.UserReview
import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTags
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTitle
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_08
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaceDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: PlaceDetailsViewModel = koinViewModel(),
) {

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
    )

    PlaceDetailsScreen(
        uiState = viewModel.uiState,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun PlaceDetailsScreen(
    uiState: UiState,
    onBackClick: () -> Unit,
    onAction: (Action) -> Unit,
) {
    PlaceDetails(
        uiState = uiState,
        onBackClick = onBackClick,
        onAction = onAction,
    )

    when (val dialog = uiState.alertDialog) {
        PlaceDetailsViewModel.AlertDialog.None -> Unit
        PlaceDetailsViewModel.AlertDialog.DiscardReview -> DiscardReviewDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            onConfirmClick = { onAction(Action.OnConfirmDiscardReviewButtonClick) }
        )
        is PlaceDetailsViewModel.AlertDialog.Error -> ErrorDialog(
            title = stringResource(dialog.title),
            message = stringResource(dialog.message),
            onConfirmButtonClick = onBackClick,
        )
    }
}

@Composable
private fun PlaceDetails(
    uiState: UiState,
    onAction: (Action) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PlaceDetailsTopAppBar(
            onBackClick = onBackClick,
            onReportPlaceClick = { onAction(Action.OnReportPlaceClick) },
            onEditPlaceClick = { onAction(Action.OnEditPlaceClick) }
        )
        when (val detailsState = uiState.detailsState) {
            DetailsState.Loading -> VUCircularProgressIndicator(visible = true)
            is DetailsState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(bottom = Spacing_08),
                    verticalArrangement = Arrangement.spacedBy(Spacing_06),
                    content = {
                        items(
                            items = detailsState.content,
                            key = { it.hashCode() },
                            itemContent = { item ->
                                when (item) {
                                    is PlaceDetailsScreenItem.Hero -> ItemDetailHero(
                                        icon = VUIcons.Store,
                                        onImageClick = {},
                                    )
                                    is PlaceDetailsScreenItem.Header -> FeatureItemTitle(
                                        title = item.title,
                                        subtitle = { RatingBar(item.rating) }
                                    )
                                    is PlaceDetailsScreenItem.AddressAndOpeningHours -> AddressAndOpeningHours(
                                        address = item.address,
                                        openingHours = item.openingHours
                                    )
                                    is PlaceDetailsScreenItem.Description -> Text(
                                        modifier = Modifier.padding(horizontal = Spacing_06),
                                        text = item.description,
                                    )
                                    is PlaceDetailsScreenItem.Tags -> FlowRowTags(tags = item.tags)
                                    is PlaceDetailsScreenItem.StaticMap -> StaticMap(
                                        marker = item.marker,
                                        cameraPositionState = item.cameraPositionState,
                                    )
                                    PlaceDetailsScreenItem.UserReview -> UserReview(
                                        userReviewState = uiState.userReviewState,
                                        onAction = onAction,
                                    )
                                    PlaceDetailsScreenItem.Reviews -> when (uiState.reviewsState) {
                                        ReviewsState.Error -> Text(text = "ERROR")
                                        ReviewsState.Loading -> Text(text = "LOADING")
                                        is ReviewsState.Success ->
                                            Reviews(
                                                reviews = uiState.reviewsState.reviews,
                                                containsUserReview = uiState.reviewsState.containsUserReview,
                                                userReviewState = uiState.userReviewState,
                                                onAction = onAction,
                                            )
                                    }
                                }
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<PlaceDetailsViewModel.SideEffect>,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                else -> {}
            }
        }.collect()
    }
}

@Composable
private fun PlaceDetailsScreenData(
    uiState: UiState,
    onAction: (Action) -> Unit,
) {

    val latlng = LatLng(-37.331928, -59.139309)
    val camera = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(
            latlng,
            16f
        )
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        item {
            ItemDetailHero(
//                imageUri = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
                icon = VUIcons.Store,
                onImageClick = {},
            )
        }
        item {
//            FeatureItemTitle(
////                title = uiState.place.name,
//                subtitle = { RatingBar(rating = 4) }
//            )
        }
        item { CoreData() }
        item { Description() }
        item { FeatureItemTags(tags) }
//        item { Map(camera) }
//        item { AddReview(onAction = onAction) }
        item {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing_06),
                    text = "Reseñas de la comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                when (uiState.reviewsState) {
                    ReviewsState.Error -> {
                        Text(text = "ERROR")
                    }
                    ReviewsState.Loading -> {
                        Text(text = "LOADING")
                    }
                    is ReviewsState.Success -> {

                    }
                }
            }
        }
    }
}

@Composable
private fun CoreData() {
    Column(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Location, contentDescription = "")
            Text(text = "Bme. Mitre 696 - Monte Grande")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Clock, contentDescription = "")
            Text(text = "Lun a Vie - 10 a 20 hs")
        }
    }
}

private val tags = listOf("Tienda", "Take away", "Sin tacc", "100% Vegan")

@Composable
private fun Description() {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_06),
        text = description,
    )
}

private const val description =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec condimentum nulla in odio tincidunt, ut blandit tellus semper. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec condimentum nulla in odio tincidunt, ut blandit tellus semper. Lorem ipsum dolor sit amet, consectetur adipiscing elit."

@Preview
@Composable
private fun PreviewPlaceDetailsScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            PlaceDetailsScreen(
                uiState = UiState(),
                onBackClick = {},
                onAction = {},
            )
        }
    }
}
