package org.codingforanimals.veganuniverse.place.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.place.shared.model.Place
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.profile.shared.model.ToggleResult
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.report_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message_try_again
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.details.usecase.PlaceDetailsUseCases
import org.codingforanimals.veganuniverse.place.presentation.navigation.selected_place_id
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import kotlin.math.roundToInt

internal class PlaceDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    flowOnCurrentUser: FlowOnCurrentUser,
    private val useCases: PlaceDetailsUseCases,
    private val verifiedOnlyUserAction: VerifiedOnlyUserAction,
) : ViewModel() {

    private val navigationEffectsChannel: Channel<NavigationEffect> = Channel()
    val navigationEffects: Flow<NavigationEffect> = navigationEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val placeGeoHashNavArg = savedStateHandle.get<String>(selected_place_id)

    sealed class PlaceState {
        data object Loading : PlaceState()
        data object UnexpectedError : PlaceState()
        data class Success(val place: Place) : PlaceState()
    }

    val placeState: StateFlow<PlaceState> = flow {
        val geoHash = placeGeoHashNavArg ?: return@flow emit(PlaceState.UnexpectedError)
        emit(
            when (val result = useCases.getPlaceDetails(geoHash)) {
                is GetPlaceDetails.Result.Success -> PlaceState.Success(result.place)
                GetPlaceDetails.Result.UnexpectedError -> PlaceState.UnexpectedError
            }
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = PlaceState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    val user = flowOnCurrentUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = null,
    )

    var newReviewState by mutableStateOf(NewReviewState())
        private set

    private val refreshUserReviewActionChannel = Channel<Unit>()
    val userReview: StateFlow<PlaceReview?> = channelFlow {
        placeGeoHashNavArg ?: return@channelFlow
        send(useCases.getCurrentUserPlaceReview(placeGeoHashNavArg).getOrNull())
        refreshUserReviewActionChannel.receiveAsFlow().collectLatest {
            send(useCases.getCurrentUserPlaceReview(placeGeoHashNavArg).getOrNull())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = null
    )

    val otherReviewsState: StateFlow<OtherReviewsState> = channelFlow {
        val geoHash =
            placeGeoHashNavArg ?: return@channelFlow send(OtherReviewsState.UnexpectedError)
        flowOnCurrentUser().collectLatest {
            val otherReviews = useCases.getPlaceReviews(geoHash)
            if (otherReviews.isFailure) {
                send(OtherReviewsState.UnexpectedError)
            } else {
                send(OtherReviewsState.Success(otherReviews.getOrNull().orEmpty()))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = OtherReviewsState.Loading,
    )

    private var toggleBookmarkEnabled: Boolean by mutableStateOf(true)
    private val toggleBookmarkActionChannel: Channel<Boolean> = Channel()
    val isBookmarked: StateFlow<Boolean> = channelFlow {
        placeGeoHashNavArg ?: return@channelFlow
        send(useCases.isPlaceBookmarked(placeGeoHashNavArg))

        toggleBookmarkActionChannel.receiveAsFlow().collectLatest { currentValue ->
            toggleBookmarkEnabled = false
            send(!currentValue)

            val result = useCases.togglePlaceBookmark(placeGeoHashNavArg, currentValue)
            handleToggleResultSideEffects(result)
            send(result.newValue)

            toggleBookmarkEnabled = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    private suspend fun handleToggleResultSideEffects(
        result: ToggleResult,
    ) {
        when (result) {
            is ToggleResult.Success -> Unit
            is ToggleResult.UnexpectedError -> {
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = unexpected_error_message_try_again,
                    )
                )
            }
        }
    }

    var alertDialogLoading: Boolean by mutableStateOf(false)
        private set

    var alertDialog: AlertDialog? by mutableStateOf(null)
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnNavigateUpClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            Action.OnReportPlaceClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        alertDialog = AlertDialog.ReportPlace
                    }
                }
            }

            Action.OnEditPlaceClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        alertDialog = AlertDialog.EditPlace
                    }
                }
            }

            is Action.NewReview -> handleNewReviewAction(action)
            Action.OnBookmarkClick -> toggleBookmark()
            is Action.Reviews.DeleteUserReview -> handleDeleteUserReviewAction(action)
            is Action.Reviews.ReportReview -> handleReportReviewAction(action)
            is Action.Reviews.ShowMoreClick -> {
                (placeState.value as? PlaceState.Success)?.let {
                    viewModelScope.launch {
                        navigationEffectsChannel.send(
                            NavigationEffect.NavigateToReviewsScreen(
                                id = it.place.geoHash ?: return@launch,
                                name = it.place.name ?: return@launch,
                                rating = it.place.rating?.roundToInt() ?: return@launch,
                                userId = action.userId
                            )
                        )
                    }
                }
            }

            Action.OnAlertDialogDismissRequest -> {
                alertDialogLoading = false
                alertDialog = null
            }

            is Action.OnEditResult -> onPlaceEditResult(action.result)
            is Action.OnReportResult -> onPlaceReportResult(action.result)
            is Action.OnUnverifiedEmailDialogResult -> onUnverifiedEmailResult(action.result)
        }
    }

    private fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        alertDialog = null
        when (result) {
            UnverifiedEmailResult.Dismissed -> Unit
            UnverifiedEmailResult.UnexpectedError -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_not_sent)
                    )
                }
            }

            UnverifiedEmailResult.VerificationEmailSent -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(
                        Snackbar(message = verification_email_sent)
                    )
                }
            }
        }
    }

    private fun onPlaceReportResult(result: ReportContentDialogResult) {
        when (result) {
            ReportContentDialogResult.Dismiss -> {
                alertDialog = null
            }

            ReportContentDialogResult.SendReport -> {
                placeGeoHashNavArg ?: return
                viewModelScope.launch {
                    alertDialogLoading = true
                    val reportResult = useCases.reportPlace(placeGeoHashNavArg)
                    alertDialogLoading = false
                    alertDialog = null
                    if (reportResult.isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(report_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(unexpected_error_message))
                    }
                }
            }
        }
    }

    private fun onPlaceEditResult(result: EditContentDialogResult) {
        when (result) {
            EditContentDialogResult.Dismiss -> {
                alertDialog = null
            }

            is EditContentDialogResult.SendEdit -> {
                placeGeoHashNavArg ?: return
                viewModelScope.launch {
                    alertDialogLoading = true
                    val editResult = useCases.editPlace(placeGeoHashNavArg, result.edition)
                    alertDialogLoading = false
                    alertDialog = null

                    if (editResult.isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(edit_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(edit_error))
                    }
                }
            }
        }
    }

    private fun toggleBookmark() {
        if (!toggleBookmarkEnabled) return
        viewModelScope.launch {
            verifiedOnlyUserAction {
                toggleBookmarkActionChannel.send(isBookmarked.value)
            }
        }
    }

    private fun handleDeleteUserReviewAction(action: Action.Reviews.DeleteUserReview) {
        when (action) {
            is Action.Reviews.DeleteUserReview.DeleteIconClick -> {
                alertDialog = AlertDialog.DeleteReview(action.reviewId)
            }

            is Action.Reviews.DeleteUserReview.DeleteConfirmButtonClick -> {
                placeGeoHashNavArg ?: return
                alertDialogLoading = true
                viewModelScope.launch {
                    when (useCases.deletePlaceReview(placeGeoHashNavArg, action.reviewId)) {
                        DeletePlaceReview.Result.UnexpectedError -> {
                            snackbarEffectsChannel.send(Snackbar(unexpected_error))
                        }

                        DeletePlaceReview.Result.Success -> {
                            refreshUserReviewActionChannel.send(Unit)
                            snackbarEffectsChannel.send(Snackbar(R.string.delete_review_success))
                        }
                    }
                    alertDialogLoading = false
                    alertDialog = null
                }
            }
        }
    }

    private fun handleReportReviewAction(action: Action.Reviews.ReportReview) {
        when (action) {
            is Action.Reviews.ReportReview.ReportConfirmButtonClick -> {
                placeGeoHashNavArg ?: return
                alertDialogLoading = true
                viewModelScope.launch {
                    if (useCases.reportPlaceReview(placeGeoHashNavArg, action.reviewId).isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(R.string.report_review_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(unexpected_error))
                    }
                    alertDialogLoading = false
                    alertDialog = null
                }
            }

            is Action.Reviews.ReportReview.ReportIconClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        alertDialog = AlertDialog.ReportReview(action.reviewId)
                    }
                }
            }
        }
    }

    private fun handleNewReviewAction(action: Action.NewReview) {
        when (action) {
            is Action.NewReview.OnRatingChange -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        newReviewState = newReviewState.copy(
                            rating = action.rating,
                            visible = true,
                        )
                    }
                }
            }

            is Action.NewReview.OnTitleChange -> {
                if (action.title.length > TITLE_MAX_LENGTH) return
                newReviewState = newReviewState.copy(
                    title = action.title,
                )
            }

            is Action.NewReview.OnDescriptionChange -> {
                if (action.description.length > DESCRIPTION_MAX_LENGTH) return
                newReviewState = newReviewState.copy(
                    description = action.description
                )
            }

            Action.NewReview.OnConfirmDiscardReviewButtonClick -> {
                alertDialog = null
                newReviewState = NewReviewState()
            }

            Action.NewReview.OnDiscardReviewIconClick -> {
                alertDialog = AlertDialog.DiscardReview
            }

            Action.NewReview.SubmitReview -> {
                attemptSubmitReview()
            }
        }
    }

    private fun attemptSubmitReview() {
        placeGeoHashNavArg ?: return
        val placeReview = PlaceReview(
            id = null,
            userId = null,
            username = null,
            rating = newReviewState.rating,
            title = newReviewState.title,
            description = newReviewState.description,
            createdAt = null,
        )
        viewModelScope.launch {
            newReviewState = newReviewState.copy(loading = true)
            if (useCases.submitPlaceReview(placeGeoHashNavArg, placeReview).isSuccess) {
                newReviewState = newReviewState.copy(visible = false)
                refreshUserReviewActionChannel.send(Unit)
                snackbarEffectsChannel.send(Snackbar(R.string.submit_review_success))
            } else {
                snackbarEffectsChannel.send(Snackbar(R.string.submit_review_unexpected_error))
            }.also {
                newReviewState = newReviewState.copy(loading = false)
            }
        }
    }

    sealed class AlertDialog {
        data object DiscardReview : AlertDialog()
        data class DeleteReview(val reviewId: String) : AlertDialog()
        data class ReportReview(val reviewId: String) : AlertDialog()
        data object ReportPlace : AlertDialog()
        data object EditPlace : AlertDialog()
        data object UnverifiedEmail : AlertDialog()
    }

    data class NewReviewState(
        val rating: Int = 0,
        val title: String = "",
        val description: String = "",
        val loading: Boolean = false,
        val visible: Boolean = false,
    ) {
        val isSubmitButtonEnabled: Boolean = title.isNotBlank()
    }

    sealed class OtherReviewsState {
        data object Loading : OtherReviewsState()
        data object UnexpectedError : OtherReviewsState()
        data class Success(
            val otherReviews: List<PlaceReview> = emptyList(),
        ) : OtherReviewsState()
    }

    sealed class Action {
        sealed class NewReview : Action() {
            data class OnRatingChange(val rating: Int) : NewReview()
            data class OnTitleChange(val title: String) : NewReview()
            data class OnDescriptionChange(val description: String) : NewReview()
            data object OnDiscardReviewIconClick : NewReview()
            data object OnConfirmDiscardReviewButtonClick : NewReview()
            data object SubmitReview : NewReview()
        }

        data class OnUnverifiedEmailDialogResult(val result: UnverifiedEmailResult) : Action()
        data class OnEditResult(val result: EditContentDialogResult) : Action()
        data class OnReportResult(val result: ReportContentDialogResult) : Action()

        sealed class Reviews : Action() {
            sealed class DeleteUserReview : Reviews() {
                data class DeleteIconClick(val reviewId: String) : DeleteUserReview()
                data class DeleteConfirmButtonClick(val reviewId: String) : DeleteUserReview()
            }

            sealed class ReportReview : Reviews() {
                data class ReportIconClick(val reviewId: String) : ReportReview()
                data class ReportConfirmButtonClick(val reviewId: String) : ReportReview()
            }

            data class ShowMoreClick(val userId: String?) : Reviews()
        }

        data object OnNavigateUpClick : Action()
        data object OnReportPlaceClick : Action()
        data object OnEditPlaceClick : Action()
        data object OnAlertDialogDismissRequest : Action()
        data object OnBookmarkClick : Action()
    }

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data class NavigateToReviewsScreen(
            val id: String,
            val name: String,
            val rating: Int,
            val userId: String?,
        ) : NavigationEffect()
    }

    companion object {
        private const val TITLE_MAX_LENGTH = 80
        private const val DESCRIPTION_MAX_LENGTH = 600
    }
}