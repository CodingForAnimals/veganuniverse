package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.domain.places.PlaceCreator
import org.codingforanimals.veganuniverse.create.domain.places.model.CreatePlaceResult
import org.codingforanimals.veganuniverse.places.entity.PlaceForm

class CreatePlaceUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val placeCreator: PlaceCreator,
    private val getUserStatus: GetUserStatus,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(form: PlaceForm) = flow {
        if (getUserStatus().value == null) {
            return@flow emit(Status.UnauthorizedUser)
        }

        emit(Status.Loading)
        when (withContext(ioDispatcher) { placeCreator.submitPlace(form) }) {
            CreatePlaceResult.Exception.PlaceAlreadyExists -> emit(Status.PlaceAlreadyExists)
            CreatePlaceResult.Exception.UnknownException -> emit(Status.UnknownError)
            CreatePlaceResult.Success -> emit(Status.Success)
        }
    }

    sealed class Status {
        data object Loading : Status()
        data object PlaceAlreadyExists : Status()
        data object UnknownError : Status()
        data object Success : Status()
        data object UnauthorizedUser : Status()
    }
}