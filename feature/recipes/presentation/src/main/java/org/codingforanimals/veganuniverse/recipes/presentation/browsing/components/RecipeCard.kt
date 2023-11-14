@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.browsing.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.R.string.like
import org.codingforanimals.veganuniverse.core.ui.R.string.report
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun RecipeCard(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    userProfileThumbnail: String,
    title: String,
    username: String,
    createdTimeAgo: String,
    onReportClick: () -> Unit,
    description: String,
    recipeImageUrl: String,
    onLikeClick: () -> Unit,
    likesCount: Int,
) {
    Card(
        modifier = modifier.clip(ShapeDefaults.Small),
        onClick = onCardClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .widthIn(max = 54.dp)
                    .aspectRatio(1f)
                    .padding(start = Spacing_05, top = Spacing_04),
                model = userProfileThumbnail,
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(R.string.user_profile_thumbnail),
            )
            Column(modifier = Modifier.padding(top = Spacing_04)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(text = "$username • $createdTimeAgo")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onReportClick) {
                VUIcon(
                    icon = VUIcons.Report,
                    contentDescription = stringResource(report)
                )
            }
        }

        Text(
            modifier = Modifier.padding(start = Spacing_05, end = Spacing_05, top = Spacing_04),
            text = description,
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .padding(start = Spacing_05, end = Spacing_05, top = Spacing_04)
                .clip(ShapeDefaults.Small),
            model = recipeImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.recipe_image)
        )

        Row(modifier = Modifier.padding(horizontal = Spacing_05)) {
            VUAssistChip(
                icon = VUIcons.Favorite,
                label = "$likesCount",
                onClick = onLikeClick,
                iconDescription = stringResource(like),
                colors = VUAssistChipDefaults.secondaryColors(),
            )
        }
    }
}


@Composable
@Preview
private fun PreviewRecipeCard() {
    VeganUniverseTheme {
        Surface {
            RecipeCard(
                onCardClick = {},
                userProfileThumbnail = "",
                title = "Titulo de receta",
                username = "Agustin Argento",
                createdTimeAgo = "2 dias",
                onReportClick = {},
                description = "Esta es una descripción de ejemplo. La receta podría ser buenísima, nunca lo sabremos. Solo es un test, repito, solo es un test.",
                recipeImageUrl = "",
                onLikeClick = {},
                likesCount = 13,
            )
        }
    }
}