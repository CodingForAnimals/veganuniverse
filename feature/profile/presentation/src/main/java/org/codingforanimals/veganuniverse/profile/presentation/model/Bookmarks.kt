package org.codingforanimals.veganuniverse.profile.presentation.model

import org.codingforanimals.veganuniverse.shared.ui.cards.SimpleCardItem

data class Bookmarks(
    val recipes: ProfileFeatureContentState<SimpleCardItem> = ProfileFeatureContentState.Loading,
)

sealed class BookmarkState {
    data object Loading : BookmarkState()
    data object Error : BookmarkState()
    data class Success(val items: List<SimpleCardItem>) : BookmarkState()
}


sealed class ProfileFeatureContentState<out T : Any> {
    data object Loading : ProfileFeatureContentState<Nothing>()
    data object Error : ProfileFeatureContentState<Nothing>()
    data class Success<out T : Any>(val items: List<T>) : ProfileFeatureContentState<T>()
}