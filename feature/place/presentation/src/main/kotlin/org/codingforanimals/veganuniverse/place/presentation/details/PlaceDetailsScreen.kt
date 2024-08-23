@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_07
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_09
import org.codingforanimals.veganuniverse.commons.place.presentation.model.fullStreetAddress
import org.codingforanimals.veganuniverse.commons.place.presentation.model.toUI
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_title
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.details.ContentDetailItem
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.AlertDialog
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.NewReviewState
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.OtherReviewsState
import org.codingforanimals.veganuniverse.place.presentation.details.composables.Actions
import org.codingforanimals.veganuniverse.place.presentation.details.composables.FlowRowTags
import org.codingforanimals.veganuniverse.place.presentation.details.composables.OpeningHours
import org.codingforanimals.veganuniverse.place.presentation.details.composables.Reviews
import org.codingforanimals.veganuniverse.place.presentation.details.composables.StaticMap
import org.codingforanimals.veganuniverse.place.presentation.home.model.PlaceMarker
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
    navigatoToReauthenticateScreen: () -> Unit,
) {
    val viewModel: PlaceDetailsViewModel = koinViewModel()
    val placeState by viewModel.placeState.collectAsStateWithLifecycle()
    val userReview by viewModel.userReview.collectAsStateWithLifecycle()
    val otherReviewsState by viewModel.otherReviewsState.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isBookmarked by viewModel.isBookmarked.collectAsStateWithLifecycle()

    PlaceDetailsScreen(
        modifier = modifier,
        placeState = placeState,
        newReviewState = viewModel.newReviewState,
        isBookmarked = isBookmarked,
        userReview = userReview,
        otherReviewsState = otherReviewsState,
        user = user,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
    )

    HandleAlertDialog(
        alertDialog = viewModel.alertDialog,
        userActionEnabled = !viewModel.alertDialogLoading,
        onAction = viewModel::onAction,
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        navigateUp = navigateUp,
        navigateToReviewsScreen = navigateToReviewsScreen,
        navigatoToReauthenticateScreen = navigatoToReauthenticateScreen,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    placeState: PlaceDetailsViewModel.PlaceState,
    snackbarHostState: SnackbarHostState,
    userReview: PlaceReview?,
    otherReviewsState: OtherReviewsState,
    isBookmarked: Boolean,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MediumTopAppBar(
                title = {
                    Crossfade(
                        targetState = placeState,
                        label = "top_bar_title_cross_fade"
                    ) { state ->
                        when (state) {
                            is PlaceDetailsViewModel.PlaceState.Success -> {
                                Text(text = state.place.name.orEmpty())
                            }

                            else -> Unit
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(Action.OnNavigateUpClick) },
                        content = {
                            Icon(
                                imageVector = VUIcons.ArrowBack.imageVector,
                                contentDescription = stringResource(
                                    id = R.string.back
                                )
                            )
                        }
                    )
                },
                actions = {
                    Actions(
                        isBookmarked = isBookmarked,
                        onBookmarkClick = { onAction(Action.OnBookmarkClick) },
                        onEditClick = { onAction(Action.OnEditPlaceClick) },
                        onReportClick = { onAction(Action.OnReportPlaceClick) },
                    )
                }
            )
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = placeState,
            label = "place_details_cross_fade"
        ) { state ->
            when (state) {
                PlaceDetailsViewModel.PlaceState.Loading -> {
                    VUCircularProgressIndicator()
                }

                PlaceDetailsViewModel.PlaceState.UnexpectedError -> {
                    AlertDialog(
                        onDismissRequest = { onAction(Action.OnNavigateUpClick) },
                        title = { Text(text = stringResource(id = unexpected_error_title)) },
                        text = { Text(text = stringResource(id = unexpected_error_message)) },
                        confirmButton = {
                            TextButton(onClick = { onAction(Action.OnNavigateUpClick) }) {
                                Text(text = stringResource(back))
                            }
                        },
                    )
                }

                is PlaceDetailsViewModel.PlaceState.Success -> {
                    PlaceDetails(
                        place = state.place,
                        userReview = userReview,
                        otherReviewsState = otherReviewsState,
                        newReviewState = newReviewState,
                        user = user,
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceDetails(
    place: Place,
    userReview: PlaceReview?,
    otherReviewsState: OtherReviewsState,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action) -> Unit,
) {
    var showImageDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        ContentDetailsHero(
            url = place.imageUrl,
            icon = place.type?.toUI()?.icon ?: VUIcons.Store,
            onImageClick = { showImageDialog = true },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            place.description?.let { description ->
                ContentDetailItem(
                    title = stringResource(id = R.string.about_this_place),
                    subtitle = description,
                    icon = VUIcons.Community.id
                )
            }

            place.addressComponents?.fullStreetAddress?.let { address ->
                ContentDetailItem(
                    title = stringResource(id = R.string.address),
                    subtitle = address,
                    icon = VUIcons.Location.id,
                    iconTint = MaterialTheme.colorScheme.onSurface
                )
            }

            val marker = remember { PlaceMarker.getMarker(place.type) }
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(place.latitude, place.longitude),
                    15f,
                )
            }
            StaticMap(
                modifier = Modifier.padding(
                    top = Spacing_04,
                    start = Spacing_05,
                    end = Spacing_05,
                ),
                marker = marker,
                cameraPositionState = cameraPositionState,
            )

            OpeningHours(
                openingHours = place.openingHours.mapNotNull { it.toUI() }
            )

            place.tags?.let {
                FlowRowTags(
                    tags = it,
                )
            }

            Reviews(
                userReview = userReview,
                otherReviewsState = otherReviewsState,
                newReviewState = newReviewState,
                user = user,
                onAction = onAction,
                rating = place.rating,
            )
        }
    }
    if (showImageDialog) {
        Dialog(
            onDismissRequest = { showImageDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing_09),
                model = place.imageUrl,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun HandleAlertDialog(
    alertDialog: AlertDialog?,
    userActionEnabled: Boolean,
    onAction: (Action) -> Unit,
) {
    when (alertDialog) {
        AlertDialog.DiscardReview -> AlertDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            text = { Text(text = stringResource(R.string.discard_review_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(Action.NewReview.OnConfirmDiscardReviewButtonClick) },
                    enabled = userActionEnabled,
                ) {
                    Text(text = stringResource(R.string.discard))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(Action.OnAlertDialogDismissRequest) },
                    enabled = userActionEnabled,
                ) {
                    Text(text = stringResource(id = R.string.back))
                }
            },
            icon = { VUIcon(icon = VUIcons.Delete) },
            containerColor = MaterialTheme.colorScheme.surface,
            iconContentColor = MaterialTheme.colorScheme.onSurface,
        )

        is AlertDialog.DeleteReview -> AlertDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            title = { Text(text = stringResource(R.string.alert_dialog_delete_review_title)) },
            text = { Text(text = stringResource(R.string.alert_dialog_delete_review_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            Action.Reviews.DeleteUserReview.DeleteConfirmButtonClick(
                                alertDialog.reviewId
                            )
                        )
                    },
                    enabled = userActionEnabled,
                    content = { Text(text = stringResource(R.string.alert_dialog_delete_review_confirm_button_label)) },
                )
            },
        )

        is AlertDialog.ReportReview -> AlertDialog(
            onDismissRequest = { onAction(Action.OnAlertDialogDismissRequest) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            Action.Reviews.ReportReview.ReportConfirmButtonClick(
                                alertDialog.reviewId
                            )
                        )
                    },
                    enabled = userActionEnabled,
                    content = { Text(text = stringResource(R.string.alert_dialog_report_review_confirm_button_label)) },
                )
            },
            title = { Text(text = stringResource(R.string.alert_dialog_report_review_title)) },
            text = { Text(text = stringResource(R.string.alert_dialog_report_review_text)) }
        )

        AlertDialog.EditPlace -> {
            EditContentDialog(
                onResult = { onAction(Action.OnEditResult(it)) }
            )
        }

        AlertDialog.ReportPlace -> {
            ReportContentDialog(onResult = { onAction(Action.OnReportResult(it)) })
        }

        AlertDialog.UnverifiedEmail -> {
            UnverifiedEmailDialog(onResult = { onAction(Action.OnUnverifiedEmailDialogResult(it)) })
        }

        null -> Unit
    }
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<PlaceDetailsViewModel.NavigationEffect>,
    navigateToAuthenticateScreen: () -> Unit,
    navigateUp: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
    navigatoToReauthenticateScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PlaceDetailsViewModel.NavigationEffect.NavigateToAuthenticateScreen -> {
                    navigateToAuthenticateScreen()
                }

                is PlaceDetailsViewModel.NavigationEffect.NavigateToReviewsScreen -> {
                    navigateToReviewsScreen(
                        sideEffect.id,
                        sideEffect.name,
                        sideEffect.rating,
                        sideEffect.userId
                    )
                }

                PlaceDetailsViewModel.NavigationEffect.NavigateUp -> navigateUp()
                PlaceDetailsViewModel.NavigationEffect.NavigateToReauthenticateScreen -> navigatoToReauthenticateScreen()
            }
        }.collect()
    }
}
