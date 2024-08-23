package org.codingforanimals.veganuniverse.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.profile.domain.model.ListingType
import org.codingforanimals.veganuniverse.commons.user.domain.model.User
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

internal class ProfileScreenViewModel(
    flowOnCurrentUser: FlowOnCurrentUser,
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    val profileState: StateFlow<ProfileState> = flowOnCurrentUser().map { user ->
        user?.let {
            ProfileState.ProfileContent(user)
        } ?: ProfileState.AuthenticatePrompt
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileState.Loading,
    )

    sealed class ProfileState {
        data object Loading : ProfileState()
        data object AuthenticatePrompt : ProfileState()
        data class ProfileContent(val user: User) : ProfileState()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.BookmarksClick.Places -> {
                viewModelScope.launch {
                        navigationEffectsChannel.send(NavigationEffect.PlaceListing(ListingType.BOOKMARKS))
                }
            }
            Action.OnLogoutClick -> logout()
            else -> Unit
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authenticationUseCases.logout()
        }
    }

    sealed class Action {
        data object OnLogoutClick : Action()
        sealed class ContributionsClick : Action() {
            data object Products : ContributionsClick()
            data object Places : ContributionsClick()
            data object Recipes : ContributionsClick()
        }
        sealed class BookmarksClick : Action() {
            data object Products : BookmarksClick()
            data object Places : BookmarksClick()
            data object Recipes : BookmarksClick()
        }
    }

    sealed class NavigationEffect {
        data class PlaceListing(val listingType: ListingType) : NavigationEffect()
    }
}