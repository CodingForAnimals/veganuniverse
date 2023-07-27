package org.codingforanimals.places.presentation.details.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.R
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.UserReviewState
import org.codingforanimals.veganuniverse.core.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.core.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme

@Composable
internal fun Reviews(
    reviewsState: PlaceDetailsViewModel.ReviewsState.Success,
    userReviewState: UserReviewState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .animateAlphaOnStart()
            .padding(horizontal = Spacing_06),
    ) {
        AnimatedVisibility(
            visible = !reviewsState.containsUserReview,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                ) {
                    Text(
                        text = stringResource(R.string.place_details_add_review_header_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    InteractiveRatingBar(
                        value = userReviewState.rating,
                        onValueChange = { onAction(Action.OnUserReviewRatingUpdate(rating = it)) },
                    )
                    AnimatedVisibility(visible = userReviewState.visible) {
                        AddReviewForm(userReviewState = userReviewState, onAction = onAction)
                    }
                }
            },
        )

        Text(
            modifier = Modifier.padding(top = Spacing_06, bottom = Spacing_04),
            text = stringResource(R.string.place_details_review_list_header_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        ReviewList(
            reviewsState = reviewsState,
            onAction = onAction,
        )
    }
}

@Composable
private fun ReviewList(
    reviewsState: PlaceDetailsViewModel.ReviewsState.Success,
    onAction: (Action) -> Unit,
) {
    Crossfade(targetState = reviewsState.isEmpty) { reviewsAreEmpty ->
        if (reviewsAreEmpty) {
            ErrorView(message = R.string.empty_reviews_message)
        } else {
            Column(
                modifier = Modifier.padding(bottom = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
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
                        icon = VUIcons.Delete,
                        onIconClick = { onAction(Action.OnDeleteReviewIconClick) }
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
                            icon = VUIcons.Report,
                            onIconClick = { onAction(Action.OnReportReviewIconClick) }
                        )
                    }
                }
                Crossfade(
                    targetState = reviewsState.loadingMore,
                    modifier = Modifier.fillMaxWidth(),
                ) { loadingMoreReviews ->
                    if (loadingMoreReviews) {
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

@Composable
internal fun Review(
    modifier: Modifier = Modifier,
    username: String,
    rating: Int,
    title: String,
    description: String?,
    date: String,
    borderStroke: BorderStroke? = null,
    icon: Icon,
    onIconClick: () -> Unit,
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
            VUIcon(
                icon = icon,
                contentDescription = "",
                onIconClick = onIconClick,
            )
        }
    )
    GenericPost(
        modifier = modifier,
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

@Composable
private fun AddReviewForm(
    modifier: Modifier = Modifier,
    image: Int = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
    userReviewState: UserReviewState,
    onAction: (Action) -> Unit,
) {
    Card(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing_02)) {

            Row(modifier = Modifier) {
                Image(
                    modifier = Modifier
                        .padding(start = Spacing_06, top = Spacing_04, end = Spacing_04)
                        .size(50.dp)
                        .clip(CircleShape),
                    painter = painterResource(image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = Spacing_04),
                    content = {
                        Text(
                            text = userReviewState.username
                                ?: stringResource(R.string.place_details_add_review_username_placeholder)
                        )
                        RatingBar(rating = userReviewState.rating)
                    },
                )
                VUIcon(
                    modifier = Modifier.padding(end = Spacing_04, top = Spacing_04),
                    icon = VUIcons.Delete,
                    contentDescription = "",
                    onIconClick = { onAction(Action.OnDiscardReviewIconClick) },
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = userReviewState.title,
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
                    .padding(horizontal = Spacing_06),
                value = userReviewState.description,
                onValueChange = { onAction(Action.OnUserReviewDescriptionUpdate(description = it)) },
                label = { Text("Contanos con más detalle") },
                colors = VUTextFieldDefaults.colors(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Crossfade(
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .padding(horizontal = Spacing_06, vertical = Spacing_02)
                    .fillMaxWidth(),
                targetState = userReviewState.loading,
                content = { isLoading ->
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .heightIn(min = 50.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        }
                    } else {
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(),
                            onClick = {
                                if (!userReviewState.loading) {
                                    onAction(Action.SubmitReview)
                                }
                            },
                            enabled = userReviewState.isSubmitButtonEnabled,
                        ) {
                            Text(text = stringResource(R.string.place_details_add_review_submit_button_label))
                        }
                    }
                },
            )

        }
    }
}

@Composable
@Preview
private fun PreviewNewForm() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            AddReviewForm(userReviewState = UserReviewState(), onAction = {})
        }
    }
}