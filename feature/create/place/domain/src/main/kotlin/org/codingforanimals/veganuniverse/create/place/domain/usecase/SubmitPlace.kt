package org.codingforanimals.veganuniverse.create.place.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
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
    }.onFailure {
        Log.e("SubmitPlace", "Error submitting place", it)
        Analytics.logNonFatalException(it)
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

    class PlaceConflictException(message: String) : Exception(message)
}
