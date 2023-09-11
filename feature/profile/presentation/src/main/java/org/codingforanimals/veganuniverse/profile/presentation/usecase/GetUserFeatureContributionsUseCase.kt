package org.codingforanimals.veganuniverse.profile.presentation.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.core.ui.place.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.profile.domain.ProfileRepository

private const val TAG = "GetUserFeatureContribut"

class GetUserFeatureContributionsUseCase(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(): Flow<UserFeatureContributionsStatus> = flow {
        emit(UserFeatureContributionsStatus.Loading)
        try {
            val user =
                userRepository.user.value ?: return@flow emit(UserFeatureContributionsStatus.Error)

            val result = profileRepository.getUserFeatureContributions(user.id)
            emit(UserFeatureContributionsStatus.Success(result.places.mapNotNull { it.toCard() }))
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(UserFeatureContributionsStatus.Error)
        }
    }

    private fun Place.toCard(): PlaceCard? {
        return try {
            val type = PlaceType.valueOf(type)
            PlaceCard(
                geoHash = geoHash,
                name = name,
                rating = rating,
                streetAddress = addressComponents.streetAddress,
                administrativeArea = addressComponents.administrativeArea,
                type = type,
                imageRef = imageRef,
                tags = tags.map { PlaceTag.valueOf(it) },
                timestamp = timestamp,
                latitude = latitude,
                longitude = longitude,
                marker = PlaceMarker.getMarker(type),
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}

sealed class UserFeatureContributionsStatus {
    data object Loading : UserFeatureContributionsStatus()
    data object Error : UserFeatureContributionsStatus()
    data class Success(
        val places: List<PlaceCard>,
    ) : UserFeatureContributionsStatus()
}

