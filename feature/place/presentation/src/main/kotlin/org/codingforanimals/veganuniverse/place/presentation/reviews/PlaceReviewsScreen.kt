@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.place.presentation.reviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.ui.components.RatingBar
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.utils.DateUtils
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.composables.Review
import org.codingforanimals.veganuniverse.place.presentation.reviews.PlaceReviewsViewModel.Action
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
internal fun PlaceReviewsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    placeName: String?,
    rating: Int?,
) {
    val viewModel: PlaceReviewsViewModel = koinViewModel()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    val userReview by viewModel.userReview.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    PlaceReviewsScreen(
        modifier = modifier,
        reviews = reviews,
        userReview = userReview,
        placeName = placeName,
        rating = rating,
        navigateUp = navigateUp,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
    )

    ReviewAlertDialog(
        reviewAlertDialog = viewModel.reviewAlertDialog,
        isLoading = viewModel.alertDialogLoading,
        onAction = viewModel::onAction,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun PlaceReviewsScreen(
    modifier: Modifier = Modifier,
    reviews: LazyPagingItems<PlaceReview>,
    userReview: PlaceReview?,
    placeName: String?,
    rating: Int?,
    navigateUp: () -> Unit = {},
    onAction: (Action) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MediumTopAppBar(
                title = {
                    placeName?.let {
                        Text(text = stringResource(R.string.place_reviews_topbar_title, it))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            rating?.let {
                RatingBar(
                    modifier = Modifier.padding(Spacing_04),
                    rating = it,
                    starIconSize = 42.dp
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
                contentPadding = PaddingValues(Spacing_04)
            ) {
                userReview?.let { review ->
                    item {
                        Review(
                            username = review.username,
                            rating = review.rating,
                            title = review.title,
                            description = review.description,
                            date = review.createdAt?.let { DateUtils.getTimeAgo(it.time) },
                            actionIcon = VUIcons.Delete.id,
                            onActionIconClick = {
                                review.id?.let {
                                    onAction(Action.DeleteUserReview.DeleteIconClick(it))
                                }
                            },
                            borderStroke = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                }
                items(reviews.itemCount) { index ->
                    val review = reviews[index] ?: return@items
                    key(review.id) {
                        Review(
                            username = review.username,
                            rating = review.rating,
                            title = review.title,
                            description = review.description,
                            date = review.createdAt?.let { DateUtils.getTimeAgo(it.time) },
                            actionIcon = VUIcons.Report.id,
                            onActionIconClick = {
                                review.id?.let {
                                    onAction(Action.ReportReview.ReportIconClick(it))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewAlertDialog(
    reviewAlertDialog: PlaceReviewsViewModel.ReviewAlertDialog?,
    isLoading: Boolean,
    onAction: (Action) -> Unit,
) {
    reviewAlertDialog?.let {
        when (it) {
            is PlaceReviewsViewModel.ReviewAlertDialog.Delete -> {
                AlertDialog(
                    onDismissRequest = { onAction(Action.DeleteUserReview.OnDismissRequest) },
                    confirmButton = {
                        Button(
                            onClick = {
                                onAction(Action.DeleteUserReview.DeleteConfirmButtonClick(it.reviewId))
                            },
                            content = {
                                Text(text = "Eliminar")
                            },
                            enabled = !isLoading,
                        )
                    },
                    title = { Text(text = "¿Quieres eliminar tu reseña?") },
                    text = { Text(text = "Podés volver a dejar otra reseña de este lugar cuando quieras.") },
                )
            }

            is PlaceReviewsViewModel.ReviewAlertDialog.Report -> {
                AlertDialog(
                    onDismissRequest = { onAction(Action.ReportReview.OnDismissRequest) },
                    confirmButton = {
                        Button(
                            onClick = {
                                onAction(Action.ReportReview.ReportConfirmButtonClick(it.reviewId))
                            },
                            content = {
                                Text(text = "Reportar")
                            },
                            enabled = !isLoading,
                        )
                    },
                    title = { Text(text = "¿Querés reportar esta reseña?") },
                    text = { Text(text = "Si detectaste alguna conducta inapropiada, reportá esta reseña.") },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPlaceReviewsScreen() {
    val reviewsPagingData = PagingData.from(
        listOf(
            PlaceReview(
                id = "1",
                userId = "userId",
                username = "User name",
                rating = 3,
                title = "Buen lugar",
                description = "Un lugar decente con opciones veganas",
                createdAt = Date(),
                placeName = "El pepe"
            ),
            PlaceReview(
                id = "2",
                userId = "userId",
                username = "User name",
                rating = 3,
                title = "Buen lugar",
                description = "Un lugar decente con opciones veganas",
                createdAt = Date(),
                placeName = "El pepe"
            ),
            PlaceReview(
                id = "3",
                userId = "userId",
                username = "User name",
                rating = 3,
                title = "Buen lugar",
                description = "Un lugar decente con opciones veganas",
                createdAt = Date(),
                placeName = "El pepe"
            ),
            PlaceReview(
                id = "4",
                userId = "userId",
                username = "User name",
                rating = 3,
                title = "Buen lugar",
                description = "Un lugar decente con opciones veganas",
                createdAt = Date(),
                placeName = "El pepe"
            ),
            PlaceReview(
                id = "5",
                userId = "userId",
                username = "User name",
                rating = 3,
                title = "Buen lugar",
                description = "Un lugar decente con opciones veganas",
                createdAt = Date(),
                placeName = "El pepe"
            )
        )
    )
    val reviews = flowOf(reviewsPagingData).collectAsLazyPagingItems()
    PlaceReviewsScreen(
        reviews = reviews,
        userReview = PlaceReview(
            id = "6",
            userId = "userId",
            username = "Logged in user review",
            rating = 3,
            title = "Buen lugar",
            description = "Un lugar decente con opciones veganas",
            createdAt = Date(),
            placeName = "El pepe"
        ),
        placeName = "Nombre de lugar en monte grande",
        rating = 5,
    )
}
