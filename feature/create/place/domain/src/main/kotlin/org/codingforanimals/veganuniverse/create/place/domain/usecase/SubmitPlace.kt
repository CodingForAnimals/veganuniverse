package org.codingforanimals.veganuniverse.create.place.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.create.place.domain.model.PlaceForm
import org.codingforanimals.veganuniverse.network.NetworkUtils
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.place.model.Place
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentUserRepository

class SubmitPlace(
    private val currentUserRepository: CurrentUserRepository,
    private val placeRepository: PlaceRepository,
    private val networkUtils: NetworkUtils,
) {
    suspend operator fun invoke(placeForm: PlaceForm): Result = runCatching {
        if (!networkUtils.isNetworkAvailable()) return Result.NoInternet
        var user = currentUserRepository.getCurrentUser() ?: return Result.GuestUser
        if (!user.isEmailVerified) {
            val refreshedUser = currentUserRepository.refreshUser() ?: return Result.GuestUser
            refreshedUser.takeIf { it.isEmailVerified }?.let { user = it }
                ?: return Result.UnverifiedEmail
        }

        val alreadyExistingPlaceId =
            placeRepository.getByLatLng(placeForm.latitude, placeForm.longitude)?.geoHash
        if (alreadyExistingPlaceId != null) {
            return Result.AlreadyExists(alreadyExistingPlaceId)
        }

        val formAsModel = placeForm.toModel(user.id, user.name)
        val placeId = placeRepository.insertPlace(formAsModel, placeForm.imageModel)
        Result.Success(placeId)
    }.getOrElse {
        Log.e(TAG, it.stackTraceToString())
        Result.UnexpectedError
    }

    private fun PlaceForm.toModel(userId: String, username: String): Place {
        return Place(
            geoHash = null,
            userId = userId,
            username = username,
            name = name,
            addressComponents = addressComponents,
            type = type,
            tags = tags,
            description = description,
            rating = 0.0,
            latitude = latitude,
            longitude = longitude,
            openingHours = openingHours,
            imageUrl = null,
            createdAt = null,
        )
    }

    sealed class Result {
        data object NoInternet : Result()
        data object GuestUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data class AlreadyExists(val placeId: String) : Result()
        data class Success(val placeId: String) : Result()
    }

    companion object {
        private const val TAG = "SubmitPlace"
    }
}
