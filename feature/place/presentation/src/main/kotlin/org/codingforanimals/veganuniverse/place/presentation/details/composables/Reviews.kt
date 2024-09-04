package org.codingforanimals.veganuniverse.place.presentation.details.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error
import org.codingforanimals.veganuniverse.commons.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.commons.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.commons.ui.components.RatingBar
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.utils.DateUtils
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.model.UserRole
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.composables.Review
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.NewReviewState
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsViewModel.OtherReviewsState
import kotlin.math.roundToInt

@Composable
internal fun Reviews(
    modifier: Modifier = Modifier,
    userReview: PlaceReview?,
    otherReviewsState: OtherReviewsState,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action) -> Unit,
    rating: Double?,
) {
    when (otherReviewsState) {
        is OtherReviewsState.UnexpectedError ->
            ErrorView(message = unexpected_error)

        OtherReviewsState.Loading ->
            VUCircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                visible = true,
            )

        is OtherReviewsState.Success ->
            ReviewsSuccessContent(
                modifier = modifier,
                userReview = userReview,
                otherReviewsState = otherReviewsState,
                newReviewState = newReviewState,
                user = user,
                onAction = onAction,
                rating = rating,
            )
    }
}

@Composable
private fun ReviewsSuccessContent(
    modifier: Modifier = Modifier,
    userReview: PlaceReview?,
    otherReviewsState: OtherReviewsState.Success,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action) -> Unit,
    rating: Double?,
) {
    Column(modifier) {
        AnimatedVisibility(
            visible = userReview == null,
            content = {
                Column(modifier = Modifier.padding(top = Spacing_04)) {
                    Text(
                        text = stringResource(R.string.place_details_add_review_header_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    InteractiveRatingBar(
                        modifier = Modifier.padding(top = Spacing_02),
                        value = newReviewState.rating,
                        onValueChange = { onAction(Action.NewReview.OnRatingChange(rating = it)) },
                    )
                    AnimatedVisibility(visible = newReviewState.visible) {
                        AddReviewForm(
                            modifier = Modifier.padding(top = Spacing_04),
                            newReviewState = newReviewState,
                            user = user,
                            onAction = onAction
                        )
                    }
                }
            },
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.place_details_review_list_header_title),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            TextButton(onClick = { onAction(Action.Reviews.ShowMoreClick(user?.id)) }) {
                Text(
                    text = "Ver más",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        rating?.let { RatingBar(rating = it.roundToInt()) }
        ReviewList(
            modifier = Modifier.padding(top = Spacing_04),
            userReview = userReview,
            otherReviewsState = otherReviewsState,
            onAction = onAction,
        )
    }
}

@Composable
private fun ReviewList(
    modifier: Modifier = Modifier,
    userReview: PlaceReview?,
    otherReviewsState: OtherReviewsState.Success,
    onAction: (Action.Reviews) -> Unit,
) {
    Crossfade(
        modifier = modifier,
        targetState = otherReviewsState.otherReviews.isEmpty() && userReview == null,
        label = "place_reviews",
    ) { reviewsAreEmpty ->
        if (reviewsAreEmpty) {
            ErrorView(message = R.string.empty_reviews_message)
        } else {
            Column(
                modifier = Modifier.padding(bottom = Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                userReview?.let { userReview ->
                    Review(
                        username = userReview.username,
                        rating = userReview.rating,
                        title = userReview.title,
                        description = userReview.description,
                        date = userReview.createdAt?.time?.let { DateUtils.getTimeAgo(it) },
                        borderStroke = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary
                        ),
                        actionIcon = VUIcons.Delete.id,
                        onActionIconClick = {
                            userReview.id?.let {
                                onAction(Action.Reviews.DeleteUserReview.DeleteIconClick(it))
                            }
                        }
                    )
                }
                otherReviewsState.otherReviews.forEachIndexed { index, review ->
                    key(index) {
                        Review(
                            modifier = Modifier.animateAlphaOnStart(),
                            username = review.username,
                            rating = review.rating,
                            title = review.title,
                            description = review.description,
                            date = review.createdAt?.time?.let { DateUtils.getTimeAgo(it) },
                            actionIcon = VUIcons.Report.id,
                            onActionIconClick = {
                                review.id?.let {
                                    onAction(Action.Reviews.ReportReview.ReportIconClick(it))
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
private fun AddReviewForm(
    modifier: Modifier = Modifier,
    newReviewState: NewReviewState,
    user: User?,
    onAction: (Action.NewReview) -> Unit,
) {
    Card(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing_02)) {
            Row(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Spacing_06, top = Spacing_04),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    content = {
                        user?.name?.let { Text(text = it) }
                        RatingBar(rating = newReviewState.rating)
                    },
                )
                VUIcon(
                    modifier = Modifier.padding(end = Spacing_04, top = Spacing_04),
                    icon = VUIcons.Delete,
                    contentDescription = "",
                    onIconClick = { onAction(Action.NewReview.OnDiscardReviewIconClick) },
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing_06),
                value = newReviewState.title,
                onValueChange = { onAction(Action.NewReview.OnTitleChange(title = it)) },
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
                value = newReviewState.description,
                onValueChange = { onAction(Action.NewReview.OnDescriptionChange(description = it)) },
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
                targetState = newReviewState.loading,
                label = "new_review",
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
                            onClick = { onAction(Action.NewReview.SubmitReview) },
                            enabled = newReviewState.isSubmitButtonEnabled,
                        ) {
                            Text(text = stringResource(R.string.place_details_add_review_submit_button_label))
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(Spacing_02))

        }
    }
}

@Composable
@Preview
private fun PreviewNewForm() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            AddReviewForm(
                newReviewState = NewReviewState(),
                user = User(
                    "123",
                    "John Doe",
                    "email@gmail.com",
                    UserRole.REGULAR,
                ),
                onAction = {})
        }
    }
}
