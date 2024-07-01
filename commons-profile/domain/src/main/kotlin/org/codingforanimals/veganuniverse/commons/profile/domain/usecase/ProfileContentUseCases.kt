package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult

interface ProfileContentUseCases {
    suspend fun isLiked(contentId: String): Boolean
    suspend fun toggleLike(contentId: String, currentValue: Boolean): ToggleResult
    suspend fun isBookmarked(contentId: String): Boolean
    suspend fun toggleBookmark(contentId: String, currentValue: Boolean): ToggleResult
    suspend fun isContributed(contentId: String): Boolean
    suspend fun addContribution(contentId: String)
    suspend fun removeContribution(contentId: String)
}
