package org.codingforanimals.veganuniverse.commons.user.data.source

import org.codingforanimals.veganuniverse.commons.user.data.dto.UserInfoDTO

interface UserInfoDataSource {
    suspend fun getUserInfo(userId: String): UserInfoDTO?
}
