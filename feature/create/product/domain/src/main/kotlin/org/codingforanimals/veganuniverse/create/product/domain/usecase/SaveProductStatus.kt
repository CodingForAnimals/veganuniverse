package org.codingforanimals.veganuniverse.create.product.domain.usecase

sealed class SaveProductStatus {
    data object Loading : SaveProductStatus()
    data class Success(val id: String) : SaveProductStatus()
    sealed class Error : SaveProductStatus() {
        data object UnregisteredUser : Error()
        data object UnverifiedEmail : Error()
        data class UnexpectedError(val error: Throwable) : Error()
        data class ProductAlreadyExists(val id: String) : Error()
    }
}
