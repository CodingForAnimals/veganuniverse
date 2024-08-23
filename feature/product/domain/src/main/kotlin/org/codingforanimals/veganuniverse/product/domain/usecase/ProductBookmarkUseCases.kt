package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases

class ProductBookmarkUseCases(
    private val profileProductUseCases: ProfileContentUseCases
) {
    suspend fun isBookmarked(productId: String): Boolean {
        return profileProductUseCases.isBookmarked(productId)
    }

    suspend fun toggleBookmark(productId: String, currentValue: Boolean): Result<Boolean> {
        return profileProductUseCases.toggleBookmark(productId, currentValue)
    }
}
