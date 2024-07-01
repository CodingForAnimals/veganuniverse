package org.codingforanimals.veganuniverse.commons.user.domain.mapper

import org.codingforanimals.veganuniverse.commons.user.data.dto.UserDTO
import org.codingforanimals.veganuniverse.commons.user.domain.model.User

internal fun UserDTO.toDomainModel(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        isEmailVerified = isEmailVerified,
    )
}
