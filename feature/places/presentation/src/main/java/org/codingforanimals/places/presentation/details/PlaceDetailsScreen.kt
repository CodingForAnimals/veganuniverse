package org.codingforanimals.places.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import org.codingforanimals.places.presentation.details.composables.ErrorView
import org.codingforanimals.places.presentation.details.composables.FlowRowTags
import org.codingforanimals.places.presentation.details.composables.PlaceDetailsTopAppBar
import org.codingforanimals.places.presentation.details.composables.Reviews
import org.codingforanimals.places.presentation.details.composables.StaticMap
import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTitle
import org.codingforanimals.veganuniverse.core.ui.shared.ItemDetailHero
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
                                    PlaceDetailsScreenItem.Reviews ->
                                        when (val reviewsState = uiState.reviewsState) {
                                            is ReviewsState.Error ->
                                                ErrorView(message = reviewsState.message)
                                            ReviewsState.Loading ->
                                                VUCircularProgressIndicator(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(200.dp),
                                                    visible = true,
                                                )
                                            is ReviewsState.Success ->
                                                Reviews(
                                                    reviewsState = reviewsState,
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
