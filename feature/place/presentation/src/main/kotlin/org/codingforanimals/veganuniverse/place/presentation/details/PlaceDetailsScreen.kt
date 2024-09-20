@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_09
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.place.presentation.model.fullStreetAddress
import org.codingforanimals.veganuniverse.commons.place.presentation.model.toUI
import org.codingforanimals.veganuniverse.commons.place.shared.model.AddressComponents
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.bookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.unbookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_title
import org.codingforanimals.veganuniverse.commons.ui.components.RatingBar
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHero
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.ContentDetailsHeroImageType
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.FeatureItemScreenTagsFlowRow
import org.codingforanimals.veganuniverse.commons.ui.contentdetails.TagItem
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.share.getShareIntent
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.AlertDialog
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.NewReviewState
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.OtherReviewsState
import org.codingforanimals.veganuniverse.place.presentation.details.composables.OpeningHours
import org.codingforanimals.veganuniverse.place.presentation.details.composables.Reviews
import org.codingforanimals.veganuniverse.place.presentation.details.composables.StaticMap
import org.codingforanimals.veganuniverse.place.presentation.home.model.PlaceMarker
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import kotlin.math.roundToInt

@Composable
internal fun PlaceDetailsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
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
        navigateUp = navigateUp,
        onAction = viewModel::onAction,
    )

    HandleAlertDialog(
        alertDialog = viewModel.alertDialog,
        userActionEnabled = !viewModel.alertDialogLoading,
        onAction = viewModel::onAction,
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateUp = navigateUp,
        navigateToReviewsScreen = navigateToReviewsScreen,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState
    )

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PlaceDetailsViewModel.SideEffect.Share -> {
                    runCatching {
                        context.startActivity(
                            getShareIntent(
                                textToShare = sideEffect.textToShare,
                                title = context.getString(R.string.share_place_title)
                            )
                        )
                    }.onFailure {
                        Analytics.logNonFatalException(it)
                    }
                }
            }
        }.collect()
    }
}

@Composable
private fun PlaceDetailsScreen(
    placeState: PlaceDetailsViewModel.PlaceState,
    otherReviewsState: OtherReviewsState,
    newReviewState: NewReviewState,
    modifier: Modifier = Modifier,
    userReview: PlaceReview? = null,
    isBookmarked: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    user: User? = null,
    navigateUp: () -> Unit = {},
    onAction: (Action) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                    ) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(Action.OnEditPlaceClick) }
                    ) {
                        VUIcon(icon = VUIcons.Edit)
                    }
                    IconButton(
                        onClick = { onAction(Action.OnReportPlaceClick) }
                    ) {
                        VUIcon(icon = VUIcons.Report)
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
                        isBookmarked = isBookmarked,
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
    otherReviewsState: OtherReviewsState,
    newReviewState: NewReviewState,
    isBookmarked: Boolean = false,
    userReview: PlaceReview? = null,
    user: User? = null,
    onAction: (Action) -> Unit = {},
) {
    var showImageDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ContentDetailsHero(
            imageType = ContentDetailsHeroImageType.Image(place.imageUrl),
            icon = place.type?.toUI()?.icon ?: VUIcons.Store,
            onImageClick = { showImageDialog = true },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_06),
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = place.name.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(
                        onClick = { onAction(Action.OnShareClick) }
                    ) {
                        VUIcon(icon = VUIcons.Share)
                    }
                    IconButton(
                        onClick = { onAction(Action.OnBookmarkClick) }
                    ) {
                        Crossfade(
                            targetState = isBookmarked,
                            label = "bookmark_cross_fade",
                            content = { bookmarked ->
                                val (icon, contentDescription) = Pair(
                                    VUIcons.BookmarkFilled,
                                    unbookmark_action
                                )
                                    .takeIf { bookmarked }
                                    ?: Pair(VUIcons.Bookmark, bookmark_action)
                                VUIcon(
                                    icon = icon,
                                    contentDescription = stringResource(id = contentDescription),
                                )
                            }
                        )
                    }
                }
                RatingBar(rating = place.rating?.roundToInt() ?: 0)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                place.addressComponents?.fullStreetAddress?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                    ) {
                        VUIcon(icon = VUIcons.Location)
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                OpeningHours(
                    openingHours = place.openingHours.mapNotNull { it.toUI() }
                )
            }

            place.description?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            place.tags?.takeIf { it.isNotEmpty() }?.let {
                FeatureItemScreenTagsFlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    tags = it.map {
                        val tag = it.toUI()
                        TagItem(tag.icon, tag.label)
                    }
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
                marker = marker,
                cameraPositionState = cameraPositionState,
            )

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
                    Text(text = stringResource(id = back))
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
    navigateUp: () -> Unit,
    navigateToReviewsScreen: (String, String, Int, String?) -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { sideEffect ->
            when (sideEffect) {
                is PlaceDetailsViewModel.NavigationEffect.NavigateToReviewsScreen -> {
                    navigateToReviewsScreen(
                        sideEffect.id,
                        sideEffect.name,
                        sideEffect.rating,
                        sideEffect.userId
                    )
                }

                PlaceDetailsViewModel.NavigationEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}

@Preview
@Composable
private fun PreviewPlaceDetailsScreen() {
    VeganUniverseTheme {
        val place = Place(
            geoHash = "123",
            userId = "333",
            username = "El pepe",
            name = "Restaurante El Pepe",
            addressComponents = AddressComponents(
                streetAddress = "Pepazo 123",
                locality = "Pepiña",
                primaryAdminArea = "Pepeland",
                secondaryAdminArea = "PepeSecondary",
                country = "Reino Pepe"
            ),
            imageUrl = null,
            type = PlaceType.RESTAURANT,
            description = "Este es un lugar hermoso, con muchas opciones veganas.",
            rating = 5.00,
            tags = listOf(
                PlaceTag.DINE_IN,
                PlaceTag.FULL_VEGAN,
            ),
            latitude = 1.00,
            longitude = 1.00,
            createdAt = Date(),
            openingHours = listOf()
        )
        PlaceDetailsScreen(
            placeState = PlaceDetailsViewModel.PlaceState.Success(place),
            otherReviewsState = OtherReviewsState.Loading,
            newReviewState = NewReviewState(),

            )
    }
}
