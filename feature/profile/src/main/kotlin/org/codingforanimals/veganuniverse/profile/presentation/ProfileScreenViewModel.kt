package org.codingforanimals.veganuniverse.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

internal class ProfileScreenViewModel(
    flowOnCurrentUser: FlowOnCurrentUser,
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

    val profileState: StateFlow<ProfileState> = flowOnCurrentUser().map { user ->
        user?.let {
            ProfileState.ProfileContent
        } ?: ProfileState.AuthenticatePrompt
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileState.AuthenticatePrompt,
    )

    sealed class ProfileState {
        data object AuthenticatePrompt : ProfileState()
        data object ProfileContent : ProfileState()
    }

    fun logout() {
        viewModelScope.launch {
            authenticationUseCases.logout()
        }
    }
}