package org.codingforanimals.veganuniverse.profile.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.auth.model.User
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard
import org.codingforanimals.veganuniverse.profile.presentation.usecase.GetUserFeatureContributionsUseCase
import org.codingforanimals.veganuniverse.profile.presentation.usecase.LogoutState
import org.codingforanimals.veganuniverse.profile.presentation.usecase.LogoutUseCase
import org.codingforanimals.veganuniverse.profile.presentation.usecase.UserFeatureContributionsStatus

internal class ProfileScreenViewModel(
    private val getUserStatus: GetUserStatus,
    private val logout: LogoutUseCase,
    private val getUserFeatureContributions: GetUserFeatureContributionsUseCase,
) : ViewModel() {

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    private var contributionsJob: Job? = null

    var uiState by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            getUserStatus().collectLatest { user ->
                uiState = if (user != null) {
                    getContributions()
                    uiState.copy(user = user)
                } else {
                    uiState.copy(user = null, userContributions = null)
                }
            }
        }
    }

    private fun getContributions() {
        contributionsJob?.cancel()
        contributionsJob = viewModelScope.launch {
            getUserFeatureContributions().collectLatest { contributions ->
                uiState = uiState.copy(
                    userContributions = when (contributions) {
                        UserFeatureContributionsStatus.Error -> ContributionsState.Error
                        UserFeatureContributionsStatus.Loading -> ContributionsState.Loading
                        is UserFeatureContributionsStatus.Success -> ContributionsState.Success(
                            contributions.places
                        )
                    }
                )
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnCreateUserButtonClick -> navigateToRegisterScreen()
            Action.LogOut -> logoutAttempt()
            Action.OnDismissErrorDialogRequest -> dismissErrorDialog()
        }
    }

    private fun navigateToRegisterScreen() {
        viewModelScope.launch {
            _sideEffect.send(SideEffect.NavigateToRegister)
        }
    }

    private fun logoutAttempt() {
        viewModelScope.launch {
            logout().collect { state ->
                uiState = when (state) {
                    LogoutState.Error -> uiState.copy(
                        loadingUser = false,
                        errorDialog = ErrorDialog(
                            errorTitle = R.string.logout_error_title,
                            errorMessage = R.string.logout_error_message
                        )
                    )

                    LogoutState.Loading -> uiState.copy(loadingUser = true)
                    LogoutState.Success -> uiState.copy(
                        user = null,
                        userContributions = null,
                        loadingUser = false
                    )
                }
            }
        }
    }

    private fun dismissErrorDialog() {
        uiState = uiState.copy(errorDialog = null)
    }

    data class UiState(
        val user: User? = null,
        val loadingUser: Boolean = false,
        val errorDialog: ErrorDialog? = null,
        val userContributions: ContributionsState? = null,
    )

    sealed class ContributionsState {
        data object Loading : ContributionsState()
        data object Error : ContributionsState()
        data class Success(val places: List<PlaceCard>) : ContributionsState() {
            val userHasNoContributions: Boolean = places.isEmpty()
        }

    }

    data class ErrorDialog(
        @StringRes val errorTitle: Int,
        @StringRes val errorMessage: Int,
    )

    sealed class Action {
        data object OnCreateUserButtonClick : Action()
        data object LogOut : Action()
        data object OnDismissErrorDialogRequest : Action()
    }

    sealed class SideEffect {
        data object NavigateToRegister : SideEffect()
    }
}