package org.codingforanimals.veganuniverse.user.domain.usecase

import org.codingforanimals.veganuniverse.user.domain.model.User

interface GetUser {
    suspend operator fun invoke(userId: String): User?
}
