package org.codingforanimals.veganuniverse.create.place.domain.usecase

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.create.place.domain.model.PlaceForm
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private typealias PlaceId = String

class SubmitPlace(
    private val placeRepository: PlaceRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val profilePlaceUseCases: ProfileContentUseCases,
) {
    suspend operator fun invoke(placeForm: PlaceForm): Result<PlaceId> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to submit a place"
        }

        val alreadyExistingPlaceId =
            placeRepository.getByLatLng(placeForm.latitude, placeForm.longitude)?.geoHash
        if (alreadyExistingPlaceId != null) {
            throw PlaceConflictException("Place already exists with geoHash: $alreadyExistingPlaceId")
        }

        val formAsModel = placeForm.toModel(user.id, user.name)
        placeRepository.insertPlace(formAsModel, placeForm.imageModel).also { placeId ->
            profilePlaceUseCases.addContribution(placeId)
        }
    }
//    suspend operator fun invoke(placeForm: PlaceForm): Result {
//        return try {
//            val user = flowOnCurrentUser().firstOrNull() ?: return Result.GuestUser
//            if (!isUserVerified()) {
//                return Result.UnverifiedEmail
//            }
//
//            val alreadyExistingPlaceId =
//                placeRepository.getByLatLng(placeForm.latitude, placeForm.longitude)?.geoHash
//            if (alreadyExistingPlaceId != null) {
//                return Result.AlreadyExists(alreadyExistingPlaceId)
//            }
//
//            val formAsModel = placeForm.toModel(user.id, user.name)
//            val placeId = placeRepository.insertPlace(formAsModel, placeForm.imageModel)
//            profilePlaceUseCases.addContribution(placeId)
//            Result.Success(placeId)
//        } catch (e: PermissionDeniedException) {
//            Result.UserMustReauthenticate
//        } catch (e: Throwable) {
//            Log.e(TAG, e.stackTraceToString())
//            Result.UnexpectedError
//        }
//    }

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

    class PlaceConflictException(message: String) : Exception(message)

//    sealed class Result {
//        data object GuestUser : Result()
//        data object UnexpectedError : Result()
//        data object UserMustReauthenticate : Result()
//        data object UnverifiedEmail : Result()
//        data class AlreadyExists(val placeId: String) : Result()
//        data class Success(val placeId: String) : Result()
//    }

    companion object {
        private const val TAG = "SubmitPlace"
    }
}
