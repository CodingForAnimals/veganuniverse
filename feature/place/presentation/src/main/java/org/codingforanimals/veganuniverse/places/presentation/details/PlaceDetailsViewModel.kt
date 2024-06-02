package org.codingforanimals.veganuniverse.places.presentation.details

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.place.details.GetPlaceDetails
import org.codingforanimals.veganuniverse.place.model.Place
import org.codingforanimals.veganuniverse.place.model.PlaceReview
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.SubmitPlaceReview
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.PlaceDetailsUseCases
import org.codingforanimals.veganuniverse.places.presentation.navigation.selected_place_id
import org.codingforanimals.veganuniverse.profile.model.ToggleResult
import org.codingforanimals.veganuniverse.user.domain.model.User
import kotlin.math.roundToInt

internal class PlaceDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCases: PlaceDetailsUseCases,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()


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

    private val mutableUserStateFlow: MutableStateFlow<User?> = MutableStateFlow(null)
    val userStateFlow: StateFlow<User?> = mutableUserStateFlow.asStateFlow()

    var newReviewState by mutableStateOf(NewReviewState())
        private set

    val reviewsState: StateFlow<ReviewsState> = flow {
        val geoHash = placeGeoHashNavArg ?: return@flow emit(ReviewsState.UnexpectedError)
        val result = useCases.getPlaceReviews(geoHash)
        if (result.userReview.isFailure || result.otherReviews.isFailure || result.user.isFailure) {
            emit(ReviewsState.UnexpectedError)
        } else {
            mutableUserStateFlow.value = result.user.getOrNull()
            emit(
                ReviewsState.Success(
                    userReview = result.userReview.getOrNull(),
                    otherReviews = result.otherReviews.getOrNull().orEmpty()
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ReviewsState.Loading,
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
            handleToggleResultSideEffects(result, ::toggleBookmark)
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
        retryAction: suspend () -> Unit,
    ) {
        when (result) {
            is ToggleResult.Success -> Unit
            is ToggleResult.GuestUser -> {
                sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
            }

            is ToggleResult.UnexpectedError -> {
                sideEffectsChannel.send(
                    SideEffect.ShowSnackbar.Error(
                        action = retryAction
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
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            Action.OnReportPlaceClick -> TODO()
            Action.OnEditPlaceClick -> TODO()

            is Action.NewReview -> handleNewReviewAction(action)
            Action.OnBookmarkClick -> toggleBookmark()
            is Action.Reviews.DeleteUserReview -> handleDeleteUserReviewAction(action)
            is Action.Reviews.ReportReview -> handleReportReviewAction(action)
            is Action.Reviews.ShowMoreClick -> {
                (placeState.value as? PlaceState.Success)?.let {
                    viewModelScope.launch {
                        sideEffectsChannel.send(
                            SideEffect.NavigateToReviewsScreen(
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
        }
    }

    private fun toggleBookmark() {
        viewModelScope.launch {
            toggleBookmarkActionChannel.send(isBookmarked.value)
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
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Error())
                        }

                        DeletePlaceReview.Result.Success -> {
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Success(R.string.delete_review_success))
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
                    when (useCases.reportPlaceReview(placeGeoHashNavArg, action.reviewId)) {
                        ReportPlaceReview.Result.UnauthenticatedUser -> {
                            sideEffectsChannel.send(SideEffect.NavigateToAuthenticateScreen)
                        }

                        ReportPlaceReview.Result.UnexpectedError -> {
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Error())
                        }

                        ReportPlaceReview.Result.Success -> {
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Success(R.string.report_review_success))
                        }
                    }
                    alertDialogLoading = false
                    alertDialog = null
                }
            }

            is Action.Reviews.ReportReview.ReportIconClick -> {
                alertDialog = AlertDialog.ReportReview(action.reviewId)
            }
        }
    }

    private fun handleNewReviewAction(action: Action.NewReview) {
        when (action) {
            is Action.NewReview.OnRatingChange -> {
                newReviewState = newReviewState.copy(
                    rating = action.rating,
                    visible = true,
                )
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
                newReviewState = newReviewState.copy(loading = true)
                viewModelScope.launch {
                    when (useCases.submitPlaceReview(placeGeoHashNavArg, placeReview)) {
                        SubmitPlaceReview.Result.UnexpectedError -> {
                            newReviewState = newReviewState.copy(loading = false)
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Error())
                        }

                        SubmitPlaceReview.Result.Success -> {
                            newReviewState = newReviewState.copy(
                                loading = false,
                                visible = false,
                            )
                            sideEffectsChannel.send(SideEffect.ShowSnackbar.Success(R.string.submit_review_success))
                        }
                    }
                }
            }
        }
    }

    sealed class AlertDialog {
        data class Error(
            @StringRes val title: Int = R.string.error_unknown_failure_title,
            @StringRes val message: Int = R.string.error_unknown_failure_message,
        ) : AlertDialog()

        data object DiscardReview : AlertDialog()
        data class DeleteReview(val reviewId: String) : AlertDialog()
        data class ReportReview(val reviewId: String) : AlertDialog()
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

    sealed class ReviewsState {
        data object Loading : ReviewsState()
        data object UnexpectedError : ReviewsState()
        data class Success(
            val userReview: PlaceReview? = null,
            val otherReviews: List<PlaceReview> = emptyList(),
        ) : ReviewsState() {
            val isEmpty = userReview == null && otherReviews.isEmpty()
        }
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

    sealed class SideEffect {
        data object NavigateToAuthenticateScreen : SideEffect()
        data object NavigateUp : SideEffect()
        data class NavigateToReviewsScreen(
            val id: String,
            val name: String,
            val rating: Int,
            val userId: String?,
        ) : SideEffect()

        sealed class ShowSnackbar : SideEffect() {
            data class Error(
                val message: Int = R.string.unexpected_error,
                val actionLabel: Int? = null,
                val action: (suspend () -> Unit)? = null,
            ) : ShowSnackbar()

            data class Success(val message: Int) : ShowSnackbar()
        }
    }

    companion object {
        private const val TITLE_MAX_LENGTH = 80
        private const val DESCRIPTION_MAX_LENGTH = 600
    }
}