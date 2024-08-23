@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.product.presentation.detail.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.R.string.bookmark_action
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit
import org.codingforanimals.veganuniverse.commons.ui.R.string.report
import org.codingforanimals.veganuniverse.commons.ui.R.string.unbookmark_action
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
internal fun ProductDetailTopBar(
    title: String,
    isBookmarked: Boolean,
    navigateUp: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    MediumTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = navigateUp,
                content = {
                    Icon(
                        imageVector = VUIcons.ArrowBack.imageVector,
                        contentDescription = stringResource(id = back),
                    )
                }
            )
        },
        actions = {
            IconButton(onClick = onBookmarkClick) {
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
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(icon.id),
                            contentDescription = stringResource(id = contentDescription),
                        )
                    }
                )
            }

            IconButton(
                onClick = onEditClick,
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = VUIcons.Edit.id),
                        contentDescription = stringResource(id = edit),
                    )
                }
            )
            IconButton(
                onClick = onReportClick,
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = VUIcons.Report.id),
                        contentDescription = stringResource(id = report),
                    )
                }
            )
        }
    )
}