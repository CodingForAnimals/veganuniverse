package org.codingforanimals.veganuniverse.user.domain.usecase

import org.codingforanimals.veganuniverse.user.domain.model.User

interface GetCurrentUser {
    suspend operator fun invoke(): User?
}
