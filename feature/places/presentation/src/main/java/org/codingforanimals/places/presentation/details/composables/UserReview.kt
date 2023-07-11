@file:OptIn(ExperimentalFoundationApi::class)

package org.codingforanimals.places.presentation.details.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.Action
import org.codingforanimals.places.presentation.details.PlaceDetailsViewModel.UserReviewState
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.InteractiveRatingBar
import org.codingforanimals.veganuniverse.core.ui.components.RatingBar
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTextFieldDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06


@Composable
internal fun LazyItemScope.UserReview(
    userReviewState: UserReviewState,
    onAction: (Action) -> Unit,
) = with(userReviewState) {
    Column(modifier = Modifier.animateItemPlacement()) {
        Text(
            text = "Aporta tu reseña",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
        )

        InteractiveRatingBar(modifier = Modifier.padding(horizontal = Spacing_06),
            value = rating,
            onValueChange = { onAction(Action.OnUserReviewRatingUpdate(rating = it)) })

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

@Composable
internal fun AddReviewPost(
    rating: Int,
    title: String,
    description: String,
    isSubmitButtonEnabled: Boolean,
    onAction: (Action) -> Unit,
) {
    val headerData = HeaderData(imageRes = R.drawable.vegan_restaurant, title = {
        Text(text = "Mi usuario")
        RatingBar(rating = rating)
    }, actions = {
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