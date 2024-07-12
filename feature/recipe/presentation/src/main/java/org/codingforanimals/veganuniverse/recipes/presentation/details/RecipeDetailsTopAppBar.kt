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
import org.codingforanimals.veganuniverse.commons.ui.R.string.bookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.delete
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit
import org.codingforanimals.veganuniverse.commons.ui.R.string.report
import org.codingforanimals.veganuniverse.commons.ui.R.string.unbookmark_action
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.recipes.presentation.R

@Composable
internal fun RecipeDetailsTopAppBar(
    recipeState: RecipeDetailsViewModel.RecipeState,
    isOwner: Boolean?,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteClick: () -> Unit
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
                            Text(text = it.recipeView.name)
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
                                isOwner = isOwner,
                                isLiked = isLiked,
                                onLikeClick = onLikeClick,
                                isBookmarked = isBookmarked,
                                onBookmarkClick = onBookmarkClick,
                                onEditClick = onEditClick,
                                onReportClick = onReportClick,
                                onDeleteClick = onDeleteClick,
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
    isOwner: Boolean?,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                    val (icon, contentDescription) = Pair(VUIcons.BookmarkFilled, unbookmark_action)
                        .takeIf { bookmarked }
                        ?: Pair(VUIcons.Bookmark, bookmark_action)
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(icon.id),
                        contentDescription = stringResource(id = contentDescription),
                    )
                }
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(VUIcons.Edit.id),
                contentDescription = stringResource(id = edit),
            )
        }
        isOwner?.let {
            if (isOwner) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(VUIcons.Delete.id),
                        contentDescription = stringResource(id = delete),
                    )
                }
            } else {
                IconButton(onClick = onReportClick) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(VUIcons.Report.id),
                        contentDescription = stringResource(id = report),
                    )
                }
            }
        }
    }
}