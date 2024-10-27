package org.codingforanimals.veganuniverse.place.presentation.reviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.presentation.navigation.selected_place_id
import org.codingforanimals.veganuniverse.place.reviews.DeletePlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetCurrentUserPlaceReview
import org.codingforanimals.veganuniverse.place.reviews.GetLatestPlaceReviewsPagingFlow
import org.codingforanimals.veganuniverse.place.reviews.ReportPlaceReview
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReview

internal class PlaceReviewsViewModel(
    savedStateHandle: SavedStateHandle,
    getLatestPlaceReviewsPagingFlow: GetLatestPlaceReviewsPagingFlow,
    getCurrentUserPlaceReview: GetCurrentUserPlaceReview,
    private val reportPlaceReview: ReportPlaceReview,
    private val deletePlaceReview: DeletePlaceReview,
    private val verifiedUserOnlyUserAction: VerifiedOnlyUserAction,
) : ViewModel() {

    private val placeIdNavArg: String? = savedStateHandle[selected_place_id]

    private val snackbarEffectChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectChannel.receiveAsFlow()

    val reviews: Flow<PagingData<PlaceReview>> =
        getLatestPlaceReviewsPagingFlow(placeIdNavArg.orEmpty())
            .catch { Log.e(TAG, it.stackTraceToString()) }


    private val userReviewSearchActionChannel: Channel<Unit> = Channel()
    val userReview: StateFlow<PlaceReview?> = channelFlow {
        userReviewSearchActionChannel.receiveAsFlow().collectLatest {
            send(getCurrentUserPlaceReview(placeIdNavArg.orEmpty()).getOrNull())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null,
    )

    var alertDialogLoading: Boolean by mutableStateOf(false)
        private set

    var reviewAlertDialog: ReviewAlertDialog? by mutableStateOf(null)
        private set

    sealed class ReviewAlertDialog {
        data class Report(val reviewId: String) : ReviewAlertDialog()
        data class Delete(val reviewId: String) : ReviewAlertDialog()
    }

    init {
        viewModelScope.launch {
            userReviewSearchActionChannel.send(Unit)
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.DeleteUserReview -> handleDeleteUserReviewAction(action)
            is Action.ReportReview -> handleReportReviewAction(action)
        }
    }

    private fun handleDeleteUserReviewAction(action: Action.DeleteUserReview) {
        when (action) {
            is Action.DeleteUserReview.DeleteIconClick -> {
                reviewAlertDialog = ReviewAlertDialog.Delete(action.reviewId)
            }

            Action.DeleteUserReview.OnDismissRequest -> {
                reviewAlertDialog = null
            }

            is Action.DeleteUserReview.DeleteConfirmButtonClick -> {
                placeIdNavArg ?: return
                alertDialogLoading = true
                viewModelScope.launch {
                    if (deletePlaceReview(placeIdNavArg, action.reviewId).isSuccess) {
                        userReviewSearchActionChannel.send(Unit)
                        snackbarEffectChannel.send(Snackbar(R.string.delete_review_success))
                    } else {
                        snackbarEffectChannel.send(Snackbar(unexpected_error))
                    }
                    alertDialogLoading = false
                    reviewAlertDialog = null
                }
            }
        }
    }

    private fun handleReportReviewAction(action: Action.ReportReview) {
        when (action) {
            is Action.ReportReview.ReportIconClick -> {
                viewModelScope.launch {
                    verifiedUserOnlyUserAction {
                        reviewAlertDialog = ReviewAlertDialog.Report(action.reviewId)
                    }
                }
            }

            Action.ReportReview.OnDismissRequest -> {
                reviewAlertDialog = null
            }

            is Action.ReportReview.ReportConfirmButtonClick -> {
                placeIdNavArg ?: return
                alertDialogLoading = true
                viewModelScope.launch {
                    if (reportPlaceReview(placeIdNavArg, action.reviewId).isSuccess) {
                        snackbarEffectChannel.send(Snackbar(R.string.report_review_success))
                    } else {
                        snackbarEffectChannel.send(Snackbar(unexpected_error))
                    }.also {
                        alertDialogLoading = false
                        reviewAlertDialog = null
                    }
                }
            }
        }
    }

    sealed class Action {
        sealed class DeleteUserReview : Action() {
            data object OnDismissRequest : DeleteUserReview()
            data class DeleteIconClick(val reviewId: String) : DeleteUserReview()
            data class DeleteConfirmButtonClick(val reviewId: String) : DeleteUserReview()
        }

        sealed class ReportReview : Action() {
            data object OnDismissRequest : ReportReview()
            data class ReportIconClick(val reviewId: String) : ReportReview()
            data class ReportConfirmButtonClick(val reviewId: String) : ReportReview()
        }
    }

    companion object {
        private const val TAG = "PlaceReviewsViewModel"
    }
}