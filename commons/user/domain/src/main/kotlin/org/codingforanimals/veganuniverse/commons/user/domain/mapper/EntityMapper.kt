package org.codingforanimals.veganuniverse.commons.user.domain.mapper

import org.codingforanimals.veganuniverse.commons.user.data.model.User as UserDTO
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.model.UserRole

internal fun UserDTO.toDomainModel(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        role = UserRole.fromString(role),
    )
}

