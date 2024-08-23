package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import org.codingforanimals.veganuniverse.commons.user.domain.model.UserInfo

interface GetUserInfo {
    suspend operator fun invoke(userId: String): UserInfo?
}
