package org.codingforanimals.veganuniverse.create.domain.model

sealed class ContentCreatorException : Exception() {
    object AlreadyExistsException : ContentCreatorException()
}
