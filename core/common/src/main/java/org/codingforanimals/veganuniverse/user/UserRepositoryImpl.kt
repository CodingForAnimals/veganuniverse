package org.codingforanimals.veganuniverse.user

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider

internal class UserRepositoryImpl(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UserRepository {

    private val _user: MutableSharedFlow<User> = MutableSharedFlow()
    override val user: SharedFlow<User> = _user

    init {
        CoroutineScope(coroutineDispatcherProvider.io()).launch {
            delay(5_000)
            _user.emit(GuestUser)
        }
    }

    override suspend fun login() = withContext(coroutineDispatcherProvider.io()) {
        val user = LoggedUser.aLoggedUser()
        _user.emit(user)
    }

    override suspend fun logout() = withContext(coroutineDispatcherProvider.io()) {
        _user.emit(GuestUser)
    }
}