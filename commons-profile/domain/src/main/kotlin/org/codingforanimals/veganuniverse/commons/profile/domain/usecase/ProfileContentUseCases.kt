package org.codingforanimals.veganuniverse.commons.profile.domain.usecase

interface ProfileContentUseCases {
    suspend fun isLiked(contentId: String): Boolean
    suspend fun toggleLike(contentId: String, currentValue: Boolean): Result<Boolean>
    suspend fun isBookmarked(contentId: String): Boolean
    suspend fun toggleBookmark(contentId: String, currentValue: Boolean): Result<Boolean>
    suspend fun isContributed(contentId: String): Boolean
    suspend fun addContribution(contentId: String): Result<Unit>
    suspend fun removeContribution(contentId: String): Result<Unit>
}
