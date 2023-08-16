package org.codingforanimals.veganuniverse.create.domain.places.model

sealed class CreatePlaceResult {
    object Success : CreatePlaceResult()
    sealed class Exception : CreatePlaceResult() {
        object PlaceAlreadyExists : Exception()
        object UnknownException : Exception()
    }
}