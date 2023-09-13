package org.codingforanimals.veganuniverse.profile.presentation

import android.net.Uri
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
import org.codingforanimals.veganuniverse.profile.presentation.usecase.UploadNewProfilePictureStatus
import org.codingforanimals.veganuniverse.profile.presentation.usecase.UploadNewProfilePictureUseCase
import org.codingforanimals.veganuniverse.profile.presentation.usecase.UserFeatureContributionsStatus

internal class ProfileScreenViewModel(
    private val getUserStatus: GetUserStatus,
    private val logout: LogoutUseCase,
    private val getUserFeatureContributions: GetUserFeatureContributionsUseCase,
    private val uploadNewProfilePictureUseCase: UploadNewProfilePictureUseCase,
) : ViewModel() {

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    private var contributionsJob: Job? = null
    private var imageCropperJob: Job? = null
    private var uploadNewProfilePictureJob: Job? = null

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
            Action.DismissErrorDialog -> dismissErrorDialog()
            Action.OnProfilePictureClick -> showImageDialog()
            Action.DismissImageDialog -> dismissImageDialog()
            Action.EditProfilePictureClick -> launchImageCropperForSelectingProfilePicture()
            is Action.NewProfilePictureSelected -> showNewProfilePictureConfirmationDialog(action.profilePicUri)
            Action.DismissNewProfilePictureConfirmationDialog -> dismissNewProfilePictureConfirmationDialog()
            Action.UpdateProfilePicture -> uploadNewProfilePicture()
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
                        loading = false,
                        errorDialog = ErrorDialog(
                            errorTitle = R.string.logout_error_title,
                            errorMessage = R.string.logout_error_message
                        )
                    )

                    LogoutState.Loading -> uiState.copy(loading = true)
                    LogoutState.Success -> uiState.copy(
                        user = null,
                        userContributions = null,
                        loading = false
                    )
                }
            }
        }
    }

    private fun dismissErrorDialog() {
        uiState = uiState.copy(errorDialog = null)
    }

    private fun showImageDialog() {
        uiState = uiState.copy(showImageDialog = true)
    }

    private fun dismissImageDialog() {
        uiState = uiState.copy(showImageDialog = false)
    }

    private fun launchImageCropperForSelectingProfilePicture() {
        imageCropperJob?.cancel()
        imageCropperJob = viewModelScope.launch {
            _sideEffect.send(SideEffect.LaunchImageCropperForSelectingProfilePicture)
        }
    }

    private fun showNewProfilePictureConfirmationDialog(newProfilePicUri: Uri) {
        uiState = uiState.copy(
            showNewProfilePictureConfirmationDialog = true,
            newProfilePicUri = newProfilePicUri,
        )
    }

    private fun dismissNewProfilePictureConfirmationDialog() {
        uiState = uiState.copy(
            showNewProfilePictureConfirmationDialog = false,
            newProfilePicUri = null,
        )
    }

    private fun uploadNewProfilePicture() {
        uploadNewProfilePictureJob?.cancel()
        uploadNewProfilePictureJob = viewModelScope.launch {
            uploadNewProfilePictureUseCase(uiState.newProfilePicUri).collect { state ->
                when (state) {
                    UploadNewProfilePictureStatus.Error -> uiState = uiState.copy(
                        loading = false,
                        showNewProfilePictureConfirmationDialog = false,
                        newProfilePicUri = null,
                        errorDialog = ErrorDialog(
                            errorTitle = R.string.upload_image_error_dialog_title,
                            errorMessage = R.string.upload_image_error_dialog_message,
                        ),
                    )

                    UploadNewProfilePictureStatus.Loading -> uiState = uiState.copy(
                        loading = true,
                        showNewProfilePictureConfirmationDialog = false,
                    )

                    UploadNewProfilePictureStatus.Success -> {
//                        delay(2000)
                        uiState = uiState.copy(
                            loading = true,
                            newProfilePicUri = null,
                            hideProfilePicture = true,
                        )
                        uiState.user?.profilePictureUrl?.let {
                            val user = uiState.user
//                            uiState = uiState.copy(user = null, loading = true)
                            _sideEffect.send(SideEffect.ReloadProfilePicture(it))
                            uiState = uiState.copy(loading = false, hideProfilePicture = false)
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val user: User? = null,
        val loading: Boolean = false,
        val errorDialog: ErrorDialog? = null,
        val userContributions: ContributionsState? = null,
        val showImageDialog: Boolean = false,
        val showNewProfilePictureConfirmationDialog: Boolean = false,
        val newProfilePicUri: Uri? = null,
        val hideProfilePicture: Boolean = false,
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
        data class NewProfilePictureSelected(val profilePicUri: Uri) : Action()
        data object OnCreateUserButtonClick : Action()
        data object LogOut : Action()
        data object DismissErrorDialog : Action()
        data object OnProfilePictureClick : Action()
        data object DismissImageDialog : Action()
        data object EditProfilePictureClick : Action()
        data object DismissNewProfilePictureConfirmationDialog : Action()
        data object UpdateProfilePicture : Action()
    }

    sealed class SideEffect {
        data object NavigateToRegister : SideEffect()
        data object LaunchImageCropperForSelectingProfilePicture : SideEffect()
        data class ReloadProfilePicture(val url: String) : SideEffect()
    }
}

private const val image_test =
    "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"