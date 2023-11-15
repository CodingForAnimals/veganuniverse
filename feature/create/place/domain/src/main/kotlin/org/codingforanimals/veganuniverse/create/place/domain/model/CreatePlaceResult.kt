package org.codingforanimals.veganuniverse.create.place.domain.model

sealed class CreatePlaceResult {
    data object Success : CreatePlaceResult()
    sealed class Exception : CreatePlaceResult() {
        data object PlaceAlreadyExists : Exception()
        data object UnknownException : Exception()
    }
}