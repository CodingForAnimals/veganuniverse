package org.codingforanimals.veganuniverse.create.product.data.model

sealed class SaveProductResult {
    data class Success(val id: String) : SaveProductResult()
    sealed class Error(open val error: Throwable? = null) : SaveProductResult() {
        data class UnexpectedError(override val error: Throwable) : Error(error)
        data class ConflictingEntityError(val id: String) : Error()
    }
}
