package org.codingforanimals.veganuniverse.places.presentation.details.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository

private const val TAG = "BookmarkPlaceUseCase"

internal class BookmarkPlaceUseCase(
    private val getUserStatus: GetUserStatus,
    private val placesRepository: PlacesRepository,
) {
    operator fun invoke(placeId: String, bookmark: Boolean): Flow<Status> = flow {
        getUserStatus().firstOrNull()?.id?.let { userId ->
            emit(Status.Loading)
            try {
                val bookmarked = if (bookmark) {
                    placesRepository.bookmarkPlaceReturningCurrent(
                        placeId = placeId,
                        userId = userId,
                    )
                } else {
                    placesRepository.unbookmarkPlaceReturningCurrent(
                        placeId = placeId,
                        userId = userId,
                    )
                }
                emit(Status.Success(bookmarked))
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                emit(Status.Error)
            }
        } ?: emit(Status.GuestUser)
    }

    fun getCurrent(placeId: String): Flow<Status> = getUserStatus().transform { user ->
        val status = if (user == null) {
            Status.GuestUser
        } else {
            emit(Status.Loading)
            try {
                val isBookmarked = placesRepository.isPlaceBookmarkedByUser(
                    placeId = placeId,
                    userId = user.id,
                )
                Status.Success(isBookmarked)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                Status.Error
            }
        }
        emit(status)
    }

    sealed class Status {
        data object Loading : Status()
        data object Error : Status()
        data object GuestUser : Status()
        data class Success(val toggled: Boolean) : Status()
    }
}