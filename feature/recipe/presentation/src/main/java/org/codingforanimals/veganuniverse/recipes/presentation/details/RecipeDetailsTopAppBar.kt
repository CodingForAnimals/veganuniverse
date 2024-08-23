@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.recipes.presentation.R

@Composable
internal fun RecipeDetailsTopAppBar(
    recipeState: RecipeDetailsViewModel.RecipeState,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit
) {
    MediumTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
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
        title = {
            Crossfade(
                targetState = recipeState,
                label = "title_cross_fade",
                content = {
                    when (it) {
                        is RecipeDetailsViewModel.RecipeState.Success -> {
                            Text(text = it.recipeView.title)
                        }

                        else -> Unit
                    }
                }
            )
        },
        actions = {
            Crossfade(
                targetState = recipeState,
                label = "actions_crossfade",
                content = {
                    when (it) {
                        is RecipeDetailsViewModel.RecipeState.Success -> {
                            Actions(
                                isLiked = isLiked,
                                onLikeClick = onLikeClick,
                                isBookmarked = isBookmarked,
                                onBookmarkClick = onBookmarkClick,
                                onEditClick = onEditClick,
                                onReportClick = onReportClick,
                            )
                        }

                        else -> Unit
                    }
                }
            )
        }
    )
}

@Composable
private fun Actions(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    Row {
        IconButton(onClick = onLikeClick) {
            Crossfade(
                targetState = isLiked,
                label = "like_cross_fade",
                content = { liked ->
                    val (icon, tint) = Pair(VUIcons.FavoriteFilled, Color.Red).takeIf { liked }
                        ?: Pair(VUIcons.Favorite, Color.Unspecified)
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(icon.id),
                        contentDescription = null,
                        tint = tint,
                    )
                }
            )
        }
        IconButton(onClick = onBookmarkClick) {
            Crossfade(
                targetState = isBookmarked,
                label = "bookmark_cross_fade",
                content = { bookmarked ->
                    val icon = VUIcons.BookmarkFilled.takeIf { bookmarked } ?: VUIcons.Bookmark
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(icon.id),
                        contentDescription = null,
                    )
                }
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(VUIcons.Edit.id),
                contentDescription = null,
            )
        }
        IconButton(onClick = onReportClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(VUIcons.Report.id),
                contentDescription = null,
            )
        }
    }
}