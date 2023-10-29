package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class Bookmarks(
    val recipes: BookmarkState = BookmarkState.Loading,
)

sealed class BookmarkState {
    data object Loading : BookmarkState()
    data object Error : BookmarkState()
    data class Success(val items: List<SimpleCardItem>) : BookmarkState()
}