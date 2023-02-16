package org.codingforanimals.veganuniverse.user

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class UserRepositoryImpl : UserRepository {

    override var user: User = GuestUser

    override suspend fun isUserLoggedIn(): Flow<User> = flow {
        delay(1000)
        emit(user)
    }

    override suspend fun login(): Flow<User> = flow {
        delay(1000)
        user = LoggedUser.aLoggedUser()
        emit(user)
    }

    override suspend fun logout(): Flow<User> = flow {
        delay(1000)
        user = GuestUser
        emit(user)
    }
}