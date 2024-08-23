package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult

class ProductBookmarkUseCases(
    private val profileProductUseCases: ProfileContentUseCases
) {
    suspend fun isBookmarked(productId: String): Boolean {
        return profileProductUseCases.isBookmarked(productId)
    }

    suspend fun toggleBookmark(productId: String, currentValue: Boolean): ToggleResult {
        return profileProductUseCases.toggleBookmark(productId, currentValue)
    }
}
