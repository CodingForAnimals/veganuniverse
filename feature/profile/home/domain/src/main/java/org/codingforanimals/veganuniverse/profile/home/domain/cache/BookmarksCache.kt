package org.codingforanimals.veganuniverse.profile.home.domain.cache

import android.util.LruCache

internal class ProfileStoredContentCache : LruCache<String, List<String>>(4 * 1024 * 1024) {
    companion object {
        const val LIKED_RECIPES_IDS = "liked_recipes_ids"
        const val BOOKMARKED_RECIPES_IDS = "bookmarked_recipes_ids"
    }
}