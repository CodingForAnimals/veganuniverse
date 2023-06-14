package org.codingforanimals.veganuniverse.create.presentation.place.usecase

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.create.domain.ContentCreator
import org.codingforanimals.veganuniverse.create.domain.ContentCreatorException
import org.codingforanimals.veganuniverse.create.domain.place.PlaceFormDomainEntity

class CreatePlaceUseCase(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val contentCreator: ContentCreator,
) {

    private val ioDispatcher = coroutineDispatcherProvider.io()

    suspend operator fun invoke(form: PlaceFormDomainEntity) = flow {
        emit(Status.Loading)
        val res = withContext(ioDispatcher) { contentCreator.createPlace(form) }
        val exception = res.exceptionOrNull()
        if (exception != null) {
            when (exception) {
                is ContentCreatorException.AlreadyExistsException -> emit(Status.PlaceAlreadyExists)
                else -> emit(Status.UnknownError)
            }
        } else {
            emit(Status.Success)
        }
    }

    sealed class Status {
        object Loading : Status()
        object PlaceAlreadyExists : Status()
        object UnknownError : Status()
        object Success : Status()
    }
}