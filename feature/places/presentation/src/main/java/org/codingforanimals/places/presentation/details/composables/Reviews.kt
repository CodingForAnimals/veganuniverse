package org.codingforanimals.places.presentation.details.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.R
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.core.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.core.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun Reviews(
    reviewsState: PlaceDetailsViewModel.ReviewsState.Success,
    userReviewState: PlaceDetailsViewModel.UserReviewState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier.animateAlphaOnStart()
    ) {
        AnimatedVisibility(
            visible = !reviewsState.containsUserReview,
            content = {
                AddReview(
                    userReviewState = userReviewState,
                    onAction = onAction,
                )
            },
        )

        OtherUsersReviews(
            reviewsState = reviewsState,
            onAction = onAction,
        )
    }
}

@Composable
private fun AddReview(
    userReviewState: PlaceDetailsViewModel.UserReviewState,
    onAction: (Action) -> Unit,
) = with(userReviewState) {
    Column {
        Text(
            text = stringResource(R.string.place_details_add_review_header_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
        )

        InteractiveRatingBar(
            modifier = Modifier.padding(horizontal = Spacing_06),
            value = rating,
            onValueChange = { onAction(Action.OnUserReviewRatingUpdate(rating = it)) },
        )

        AnimatedVisibility(visible = visible) {
            AddReviewForm(
                rating = rating,
                title = title,
                description = description,
                isSubmitButtonEnabled = isSubmitButtonEnabled,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun AddReviewForm(
    rating: Int,
    title: String,
    description: String,
    isSubmitButtonEnabled: Boolean,
    onAction: (Action) -> Unit,
) {
    val headerData = HeaderData(
        imageRes = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
        title = {
            Text(text = "Mi usuario")
            RatingBar(rating = rating)
        },
        actions = {
            VUIcon(
                icon = VUIcons.Delete,
                contentDescription = "",
                onIconClick = { onAction(Action.OnDiscardReviewIconClick) },
            )
        })
    GenericPost(
        modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
        headerData = headerData,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(),
        content = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_05),
                value = title,
                onValueChange = { onAction(Action.OnUserReviewTitleUpdate(title = it)) },
                label = { Text("¿Cómo fue tu experiencia?") },
                colors = VUTextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
                    .padding(horizontal = Spacing_05),
                value = description,
                onValueChange = { onAction(Action.OnUserReviewDescriptionUpdate(description = it)) },
                label = { Text("Contanos con más detalle") },
                colors = VUTextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        },
        actions = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06, vertical = Spacing_04),
                colors = ButtonDefaults.buttonColors(),
                onClick = { onAction(Action.SubmitReview) },
                enabled = isSubmitButtonEnabled,
            ) {
                Text(text = "Publicar reseña")
            }
        },
    )
}

@Composable
private fun OtherUsersReviews(
    reviewsState: PlaceDetailsViewModel.ReviewsState.Success,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = Spacing_06)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = Spacing_06),
            text = stringResource(R.string.place_details_review_list_header_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Crossfade(targetState = reviewsState.isEmpty) { reviewsAreEmpty ->
            if (reviewsAreEmpty) {
                ErrorView(message = R.string.empty_reviews_message)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing_06)) {
                    reviewsState.userReview?.let { userReview ->
                        Review(
                            username = userReview.username,
                            rating = userReview.rating,
                            title = userReview.title,
                            description = userReview.description,
                            date = userReview.timestamp,
                            borderStroke = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.primary
                            ),
                        )
                    }
                    reviewsState.otherReviews.forEachIndexed { index, review ->
                        key(index) {
                            Review(
                                modifier = Modifier.animateAlphaOnStart(),
                                username = review.username,
                                rating = review.rating,
                                title = review.title,
                                description = review.description,
                                date = review.timestamp,
                            )
                        }
                    }
                    Crossfade(
                        targetState = reviewsState.loadingMore,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (it) {
                            VUCircularProgressIndicator(visible = true)
                        } else {
                            if (reviewsState.hasMoreReviews) {
                                OutlinedButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally),
                                    onClick = { onAction(Action.OnGetMoreReviewsButtonClick) },
                                    content = {
                                        Text(text = stringResource(R.string.place_details_reviews_load_more))
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun Review(
    modifier: Modifier = Modifier,
    username: String,
    rating: Int,
    title: String,
    description: String?,
    date: String,
    borderStroke: BorderStroke? = null,
) {
    val header = HeaderData(
        imageRes = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
        title = {
            Column {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                RatingBar(rating = rating)
            }
        },
        actions = {
            val icon = if (rating == 4) {
                VUIcons.Delete
            } else {
                VUIcons.Report
            }
            VUIcon(
                icon = icon,
                contentDescription = "",
                onIconClick = {},
            )
        }
    )
    GenericPost(
        modifier = modifier.padding(horizontal = Spacing_06),
        headerData = header,
        border = borderStroke,
        content = {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            description?.let {
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = date)
        },
    )
}