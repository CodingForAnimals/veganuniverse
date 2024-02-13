package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import org.codingforanimals.veganuniverse.commons.user.data.source.UserInfoDataSource
import org.codingforanimals.veganuniverse.commons.user.domain.mapper.toModel
import org.codingforanimals.veganuniverse.commons.user.domain.model.UserInfo

internal class GetUserInfoImpl(
    private val dataSource: UserInfoDataSource,
) : GetUserInfo {

    override suspend fun invoke(userId: String): UserInfo? {
        return dataSource.getUserInfo(userId)?.toModel()
    }
}
