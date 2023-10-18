package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import android.util.Log
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.UserRepository
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.domain.places.PlaceCreator
import org.codingforanimals.veganuniverse.create.domain.places.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.create.presentation.place.CreatePlaceViewModel
import org.codingforanimals.veganuniverse.create.presentation.place.entity.toAddressComponents
import org.codingforanimals.veganuniverse.create.presentation.place.entity.toDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

private const val TAG = "SubmitPlaceUseCase"

internal class SubmitPlaceUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placeCreator: PlaceCreator,
    private val userRepository: UserRepository,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    operator fun invoke(uiState: CreatePlaceViewModel.UiState) = flow {
        val user = userRepository.user.value ?: return@flow emit(SubmitPlaceStatus.UnauthorizedUser)

        if (!user.isEmailVerified) {
            emit(SubmitPlaceStatus.Loading)
            val updatedUser =
                userRepository.refreshUser() ?: return@flow emit(SubmitPlaceStatus.UnauthorizedUser)
            if (!updatedUser.isEmailVerified) {
                return@flow emit(SubmitPlaceStatus.UnverifiedEmail)
            }
        }

        val form = uiState.toPlaceForm(user.id) ?: return@flow emit(SubmitPlaceStatus.FormError)

        emit(SubmitPlaceStatus.Loading)
        when (withContext(ioDispatcher) { placeCreator.submitPlace(form) }) {
            CreatePlaceResult.Exception.PlaceAlreadyExists -> emit(SubmitPlaceStatus.PlaceAlreadyExists)
            CreatePlaceResult.Exception.UnknownException -> emit(SubmitPlaceStatus.UnknownError)
            CreatePlaceResult.Success -> emit(SubmitPlaceStatus.Success)
        }
    }
}

private fun CreatePlaceViewModel.UiState.toPlaceForm(userId: String): PlaceForm? {
    return try {
        val latLng = locationField.latLng!!
        val addressComponents = addressField?.toAddressComponents()!!
        PlaceForm(
            name = nameField.value.ifEmpty { throw IllegalArgumentException() },
            userId = userId,
            addressComponents = addressComponents,
            description = descriptionField.value,
            openingHours = openingHoursField.sortedOpeningHours.toDomainEntity(),
            type = typeField.value?.name!!,
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            tags = selectedTagsField.tags.map { it.name },
            image = pictureField.model!!
        )
    } catch (e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        null
    }
}

sealed class SubmitPlaceStatus {
    data object Loading : SubmitPlaceStatus()
    data object PlaceAlreadyExists : SubmitPlaceStatus()
    data object UnknownError : SubmitPlaceStatus()
    data object Success : SubmitPlaceStatus()
    data object UnauthorizedUser : SubmitPlaceStatus()
    data object UnverifiedEmail : SubmitPlaceStatus()
    data object FormError : SubmitPlaceStatus()
}