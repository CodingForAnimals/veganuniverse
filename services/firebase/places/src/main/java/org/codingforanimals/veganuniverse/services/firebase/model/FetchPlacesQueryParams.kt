package org.codingforanimals.veganuniverse.services.firebase.model

data class FetchPlacesQueryParams(
    val userId: String?,
) {

    class Builder {
        private var userId: String? = null

        fun userId(userId: String): Builder {
            this.userId = userId
            return this
        }

        fun build(): FetchPlacesQueryParams {
            return FetchPlacesQueryParams(userId = userId)
        }
    }
}