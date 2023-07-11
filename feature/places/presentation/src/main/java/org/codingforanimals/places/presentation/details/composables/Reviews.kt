package org.codingforanimals.places.presentation.details.composables

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.R
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.places.presentation.model.ReviewViewEntity
import org.codingforanimals.veganuniverse.core.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUImage
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.icons.VUImages
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06

@Composable
internal fun Reviews(
    reviews: List<ReviewViewEntity>,
    userReviewState: PlaceDetailsViewModel.UserReviewState,
    containsUserReview: Boolean,
    onAction: (Action) -> Unit,
) {
    with(userReviewState) {
        AnimatedVisibility(visible = !containsUserReview) {
            Column {
                Text(
                    text = "Aporta tu reseña",
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
                    AddReviewPost(
                        rating = rating,
                        title = title,
                        description = description,
                        isSubmitButtonEnabled = isSubmitButtonEnabled,
                        onAction = onAction,
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = Spacing_06)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = Spacing_06),
            text = "Reseñas de la comunidad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Crossfade(targetState = reviews.isEmpty()) { reviewsAreEmpty ->
            if (reviewsAreEmpty) {
                EmptyReviews(message = R.string.empty_reviews_message)
            } else {
                reviews.forEachIndexed { index, review ->
                    key(index) {
                        val borderStroke = if (containsUserReview && index == 0) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        } else null
                        Review(
                            username = review.username,
                            rating = review.rating,
                            title = review.title,
                            description = review.description,
                            date = review.timestamp,
                            borderStroke = borderStroke,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun EmptyReviews(
    @StringRes message: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VUImage(
            modifier = Modifier
                .size(200.dp)
                .padding(top = Spacing_06),
            image = VUImages.ErrorCat,
        )
        Text(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(bottom = Spacing_06),
            textAlign = TextAlign.Center,
            text = stringResource(message)
        )
    }
}

@Composable
internal fun Review(
    username: String,
    rating: Int,
    title: String,
    description: String?,
    date: String,
    borderStroke: BorderStroke?,
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
        modifier = Modifier.padding(horizontal = Spacing_06),
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