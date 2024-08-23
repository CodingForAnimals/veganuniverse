package org.codingforanimals.veganuniverse.user.domain.mapper

import org.codingforanimals.veganuniverse.user.data.dto.UserDTO
import org.codingforanimals.veganuniverse.user.domain.model.User

internal fun UserDTO.toDomainModel(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        isEmailVerified = isEmailVerified,
    )
}
