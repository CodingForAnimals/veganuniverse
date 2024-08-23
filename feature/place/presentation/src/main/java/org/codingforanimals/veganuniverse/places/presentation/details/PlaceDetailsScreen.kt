@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.places.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import org.codingforanimals.veganuniverse.place.model.Place
import org.codingforanimals.veganuniverse.place.model.fullStreetAddress
import org.codingforanimals.veganuniverse.place.model.toUI
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.AlertDialog
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.NewReviewState
import org.codingforanimals.veganuniverse.places.presentation.details.PlaceDetailsViewModel.ReviewsState
import org.codingforanimals.veganuniverse.places.presentation.details.composables.Address
import org.codingforanimals.veganuniverse.places.presentation.details.composables.Description
import org.codingforanimals.veganuniverse.places.presentation.details.composables.ErrorDialog
import org.codingforanimals.veganuniverse.places.presentation.details.composables.FlowRowTags
import org.codingforanimals.veganuniverse.places.presentation.details.composables.OpeningHours
import org.codingforanimals.veganuniverse.places.presentation.details.composables.Reviews
import org.codingforanimals.veganuniverse.places.presentation.details.composables.StaticMap
import org.codingforanimals.veganuniverse.places.presentation.home.model.PlaceMarker
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_07
import org.codingforanimals.veganuniverse.ui.Spacing_08
import org.codingforanimals.veganuniverse.ui.Spacing_09
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.ui.shared.ItemDetailHero
import org.codingforanimals.veganuniverse.user.domain.model.User
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
) {
    val viewModel: PlaceDetailsViewModel = koinViewModel()
    val placeState by viewModel.placeState.collectAsStateWithLifecycle()
    val reviewsState by viewModel.reviewsState.collectAsStateWithLifecycle()
    val user by viewModel.userStateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isBookmarked by viewModel.isBookmarked.collectAsStateWithLifecycle()

    PlaceDetailsScreen(
        modifier = modifier,
        placeState = placeState,
        newReviewState = viewModel.newReviewState,
        isBookmarked = isBookmarked,
        reviewsState = reviewsState,
        user = user,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
    )

    HandleAlertDialog(
        alertDialog = viewModel.alertDialog,
        userActionEnabled = !viewModel.alertDialogLoading,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        navigateUp = navigateUp,
        navigateToReviewsScreen = navigateToReviewsScreen,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    placeState: PlaceDetailsViewModel.PlaceState,
    snackbarHostState: SnackbarHostState,
    reviewsState: ReviewsState,
    isBookmarked: Boolean,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
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
                actions = {
                    Crossfade(
                        targetState = placeState,
                        label = "top_bar_actions_cross_fade"
                    ) { state ->
                        when (state) {
                            is PlaceDetailsViewModel.PlaceState.Success -> {
                                Row {
                                    IconButton(onClick = { onAction(Action.OnBookmarkClick) }) {
                                        val icon = remember(isBookmarked) {
                                            if (isBookmarked) VUIcons.BookmarkFilled.id
                                            else VUIcons.Bookmark.id
                                        }
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(icon),
                                            contentDescription = null,
                                        )
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(VUIcons.Edit.id),
                                            contentDescription = null,
                                        )
                                    }
                                    IconButton(onClick = {}) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(VUIcons.Report.id),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }

                            else -> Unit
                        }
                    }
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
                        confirmButton = {
                            TextButton(onClick = { onAction(Action.OnNavigateUpClick) }) {
                                Text(text = stringResource(R.string.back))
                            }
                        },
                    )
                }

                is PlaceDetailsViewModel.PlaceState.Success -> {
                    PlaceDetails(
                        place = state.place,
                        reviewsState = reviewsState,
                        newReviewState = newReviewState,
                        isBookmarked = isBookmarked,
                        user = user,
                        onAction = onAction,
                    )
                }
            }
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
            text = { Text(text = "¿Quieres descartar esta reseña?") },
            confirmButton = {
                TextButton(
                    onClick = { onAction(Action.NewReview.OnConfirmDiscardReviewButtonClick) },
                    enabled = userActionEnabled,
                ) {
                    Text(text = "Descartar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(Action.OnAlertDialogDismissRequest) },
                    enabled = userActionEnabled,
                ) {
                    Text(text = "Volver")
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

        is AlertDialog.Error -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(stringResource(alertDialog.title)) },
                text = { Text(stringResource(alertDialog.message)) },
                confirmButton = {
                    TextButton(
                        onClick = { onAction(Action.OnAlertDialogDismissRequest) },
                        enabled = userActionEnabled,
                    ) {
                        Text(stringResource(org.codingforanimals.veganuniverse.core.common.R.string.back))
                    }
                },
            )
        }

        null -> Unit
    }
}

@Composable
private fun PlaceDetails(
    place: Place,
    reviewsState: ReviewsState,
    newReviewState: NewReviewState,
    isBookmarked: Boolean,
    user: User?,
    onAction: (Action) -> Unit,
) {
    var showImageDialog by remember { mutableStateOf(false) }
    LazyColumn(
        content = {
            item {
                ItemDetailHero(
                    icon = place.type?.toUI()?.icon ?: VUIcons.Store,
                    onImageClick = { showImageDialog = true },
                    url = place.imageUrl,
                )
            }

            place.description?.let {
                item {
                    Description(
                        modifier = Modifier.padding(
                            start = Spacing_04,
                            end = Spacing_04,
                        ),
                        description = it,
                    )
                }
            }

            item {
                place.addressComponents?.fullStreetAddress?.let {
                    Address(
                        modifier = Modifier.padding(
                            top = Spacing_07,
                            start = Spacing_04,
                            end = Spacing_04,
                        ),
                        fullStreetAddress = it,
                    )
                }
            }

            item {
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
                        start = Spacing_08,
                        end = Spacing_08,
                    ),
                    marker = marker,
                    cameraPositionState = cameraPositionState,
                )
            }

            item {
                OpeningHours(
                    modifier = Modifier.padding(
                        top = Spacing_07,
                        start = Spacing_04,
                        end = Spacing_04,
                    ),
                    openingHours = place.openingHours.mapNotNull { it.toUI() }
                )
            }

            place.tags?.let {
                item {
                    FlowRowTags(
                        modifier = Modifier.padding(
                            top = Spacing_07,
                            start = Spacing_04,
                            end = Spacing_04,
                        ),
                        tags = it,
                    )
                }
            }

            item {
                Reviews(
                    modifier = Modifier.padding(
                        top = Spacing_07,
                        start = Spacing_04,
                        end = Spacing_04,
                    ),
                    reviewsState = reviewsState,
                    newReviewState = newReviewState,
                    user = user,
                    onAction = onAction,
                )
            }
        },
    )

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
private fun HandleSideEffects(
    sideEffects: Flow<PlaceDetailsViewModel.SideEffect>,
    navigateToAuthenticateScreen: () -> Unit,
    navigateUp: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PlaceDetailsViewModel.SideEffect.NavigateToAuthenticateScreen -> {
                    navigateToAuthenticateScreen()
                }

                is PlaceDetailsViewModel.SideEffect.NavigateToReviewsScreen -> {
                    navigateToReviewsScreen(
                        sideEffect.id,
                        sideEffect.name,
                        sideEffect.rating,
                        sideEffect.userId
                    )
                }

                PlaceDetailsViewModel.SideEffect.NavigateUp -> navigateUp()
                is PlaceDetailsViewModel.SideEffect.ShowSnackbar.Error -> {
                    val snackActionResult = snackbarHostState.showSnackbar(
                        message = context.getString(sideEffect.message),
                        actionLabel = sideEffect.actionLabel?.let { context.getString(it) },
                        duration = SnackbarDuration.Short,
                    )
                    when (snackActionResult) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> sideEffect.action?.invoke()
                    }
                }

                is PlaceDetailsViewModel.SideEffect.ShowSnackbar.Success -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(sideEffect.message),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        }.collect()
    }
}
