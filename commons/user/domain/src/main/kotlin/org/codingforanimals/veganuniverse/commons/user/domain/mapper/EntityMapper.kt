package org.codingforanimals.veganuniverse.commons.user.domain.mapper

import org.codingforanimals.veganuniverse.commons.user.data.model.User as UserDTO
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

internal fun UserDTO.toDomainModel(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        isEmailVerified = isEmailVerified,
    )
}
