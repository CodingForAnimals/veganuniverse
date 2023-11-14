package org.codingforanimals.veganuniverse.places.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.AlertDialog
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.DetailsState
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.ReviewsState
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.UiState
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.UserReviewState
import org.codingforanimals.veganuniverse.places.presentation.details.composables.AddressAndOpeningHours
import org.codingforanimals.veganuniverse.places.presentation.details.composables.DiscardReviewDialog
import org.codingforanimals.veganuniverse.places.presentation.details.composables.ErrorDialog
import org.codingforanimals.veganuniverse.places.presentation.details.composables.ErrorView
import org.codingforanimals.veganuniverse.places.presentation.details.composables.FlowRowTags
import org.codingforanimals.veganuniverse.places.presentation.details.composables.PlaceDetailsTopAppBar
import org.codingforanimals.veganuniverse.places.presentation.details.composables.Reviews
import org.codingforanimals.veganuniverse.places.presentation.details.composables.StaticMap
import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.icon.ToggleIconState
import org.codingforanimals.veganuniverse.ui.icon.ToggleableIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.ui.shared.ItemDetailHero
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaceDetailsScreen(
    onBackClick: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    viewModel: PlaceDetailsViewModel = koinViewModel(),
) {

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
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
    Column {
        PlaceDetailsTopAppBar(
            onBackClick = onBackClick,
            onReportPlaceClick = { onAction(Action.OnReportPlaceClick) },
            onEditPlaceClick = { onAction(Action.OnEditPlaceClick) }
        )

        when (uiState.detailsState) {
            DetailsState.Loading -> VUCircularProgressIndicator(visible = true)
            DetailsState.Error -> ErrorDialog(
                title = stringResource(R.string.error_unknown_failure_title),
                message = stringResource(R.string.error_unknown_failure_message),
                onConfirmButtonClick = onBackClick,
            )

            is DetailsState.Success -> {
                PlaceDetails(
                    detailsState = uiState.detailsState,
                    onAction = onAction,
                    reviewsState = uiState.reviewsState,
                    userReviewState = uiState.userReviewState,
                    bookmarkState = uiState.bookmarkState,
                )
                uiState.alertDialog?.let { alertDialog ->
                    HandleAlertDialog(alertDialog, onAction)
                }
            }
        }
    }

    VUCircularProgressIndicator(visible = uiState.loading)
}

@Composable
private fun HandleAlertDialog(
    alertDialog: AlertDialog,
    onAction: (Action) -> Unit,
) {
    when (alertDialog) {
        AlertDialog.DiscardReview -> DiscardReviewDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            onConfirmClick = { onAction(Action.OnConfirmDiscardReviewButtonClick) }
        )

        is AlertDialog.DeleteReview -> AlertDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            title = { Text(text = stringResource(R.string.alert_dialog_delete_review_title)) },
            text = { Text(text = stringResource(R.string.alert_dialog_delete_review_text)) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(Action.OnConfirmDeleteReviewButtonClick(alertDialog.review)) },
                    content = { Text(text = stringResource(R.string.alert_dialog_delete_review_confirm_button_label)) },
                )
            },
        )

        AlertDialog.ReportReview -> AlertDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(Action.OnConfirmReportReviewButtonClick) },
                    content = { Text(text = stringResource(R.string.alert_dialog_report_review_confirm_button_label)) },
                )
            },
            title = { Text(text = stringResource(R.string.alert_dialog_report_review_title)) },
            text = { Text(text = stringResource(R.string.alert_dialog_report_review_text)) }
        )

        is AlertDialog.Error -> ErrorDialog(
            title = stringResource(alertDialog.title),
            message = stringResource(alertDialog.message),
            onConfirmButtonClick = { onAction(Action.OnAlertDialogDismissRequest) },
        )
    }
}

@Composable
private fun PlaceDetails(
    detailsState: DetailsState.Success,
    reviewsState: ReviewsState,
    userReviewState: UserReviewState,
    bookmarkState: ToggleIconState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Spacing_06),
        content = {
            items(
                items = detailsState.content,
                itemContent = { item ->
                    when (item) {
                        is PlaceDetailsScreenItem.Hero -> ItemDetailHero(
                            icon = VUIcons.Store,
                            onImageClick = {},
                            url = item.url
                        )

                        is PlaceDetailsScreenItem.Header -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing_06),
//                    verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = item.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                ToggleableIcon(
                                    state = bookmarkState,
                                    onIconClick = { onAction(Action.OnBookmarkClick) },
                                    onIcon = VUIcons.BookmarkFilled,
                                    onTint = MaterialTheme.colorScheme.primary,
                                    offIcon = VUIcons.Bookmark,
                                )
                            }
                        }

                        is PlaceDetailsScreenItem.AddressAndOpeningHours -> AddressAndOpeningHours(
                            addressComponents = item.addressComponents,
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
                            when (reviewsState) {
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
                                        userReviewState = userReviewState,
                                        onAction = onAction,
                                    )
                            }
                    }
                },
            )
        },
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<PlaceDetailsViewModel.SideEffect>,
    navigateToAuthenticateScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PlaceDetailsViewModel.SideEffect.NavigateToAuthenticateScreen -> {
                    navigateToAuthenticateScreen()
                }
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
