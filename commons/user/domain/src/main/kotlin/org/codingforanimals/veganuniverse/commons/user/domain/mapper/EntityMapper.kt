package org.codingforanimals.veganuniverse.commons.user.domain.mapper

import org.codingforanimals.veganuniverse.commons.user.data.dto.UserInfoDTO
import org.codingforanimals.veganuniverse.commons.user.domain.model.UserInfo

internal fun UserInfoDTO.toModel(): UserInfo {
    return UserInfo(
        userId = userId,
        name = name,
        email = email
    )
}
