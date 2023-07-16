package org.codingforanimals.places.presentation.details

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.places.presentation.R
import org.codingforanimals.places.presentation.details.model.DeleteUserReviewStatus
import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.places.presentation.details.model.SubmitReviewStatus
import org.codingforanimals.places.presentation.details.usecase.DeleteUserReviewUseCase
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsScreenContent
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsUseCase
import org.codingforanimals.places.presentation.details.usecase.GetPlaceReviewsUseCase
import org.codingforanimals.places.presentation.details.usecase.SubmitReviewUseCase
import org.codingforanimals.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.places.presentation.model.ReviewViewEntity
import org.codingforanimals.places.presentation.navigation.selected_place_id

internal class PlaceDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPlaceDetailsScreenContent: GetPlaceDetailsScreenContent,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase,
    private val getPlaceReviewsUseCase: GetPlaceReviewsUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val deleteUserReviewUseCase: DeleteUserReviewUseCase,
) : ViewModel() {

    private var submitReviewJob: Job? = null
    private var fetchReviewsJob: Job? = null
    private var deleteUserReviewJob: Job? = null

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    private val placeId = checkNotNull(savedStateHandle.get<String>(selected_place_id))

    init {
        viewModelScope.launch {
            getPlaceDetailsUseCase(placeId).collectLatest { status ->
                when (status) {
                    GetPlaceDetailsStatus.Loading -> {
                        uiState = uiState.copy(detailsState = DetailsState.Loading)
                    }
                    is GetPlaceDetailsStatus.Exception -> {
                        uiState = uiState.copy(detailsState = DetailsState.Error)
                    }
                    is GetPlaceDetailsStatus.Success -> {
                        val content = getPlaceDetailsScreenContent(status.place)
                        uiState = uiState.copy(detailsState = DetailsState.Success(content))
                        fetchReviews()
                    }
                }
            }
        }
    }

    private fun fetchReviews() {
        fetchReviewsJob?.cancel()
        fetchReviewsJob = viewModelScope.launch {
            getPlaceReviewsUseCase(placeId).collectLatest { status ->
                val state = when (status) {
                    GetPlaceReviewsStatus.Loading -> ReviewsState.Loading
                    is GetPlaceReviewsStatus.Exception -> ReviewsState.Error(status.error)
                    is GetPlaceReviewsStatus.Success -> ReviewsState.Success(
                        userReview = status.userReview,
                        otherReviews = status.paginatedReviews,
                        hasMoreReviews = status.hasMoreReviews,
                    )
                }
                uiState = uiState.copy(reviewsState = state)
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnReportPlaceClick -> Unit
            Action.OnEditPlaceClick -> Unit
            Action.SubmitReview -> submitReview()
            Action.OnDiscardReviewIconClick -> uiState = uiState.copy(
                alertDialog = AlertDialog.DiscardReview,
            )
            Action.OnConfirmDiscardReviewButtonClick -> uiState = uiState.copy(
                alertDialog = null,
                userReviewState = UserReviewState(),
            )
            is Action.OnUserReviewDescriptionUpdate -> updateReviewDescription(action.description)
            is Action.OnUserReviewRatingUpdate -> updateReviewRating(action.rating)
            is Action.OnUserReviewTitleUpdate -> updateReviewTitle(action.title)
            Action.OnAlertDialogDismissRequest -> uiState =
                uiState.copy(alertDialog = null)
            Action.OnGetMoreReviewsButtonClick -> getMoreReviews()
            Action.OnDeleteReviewIconClick -> uiState =
                uiState.copy(alertDialog = AlertDialog.DeleteReview)
            Action.OnReportReviewIconClick -> uiState =
                uiState.copy(alertDialog = AlertDialog.ReportReview)
            Action.OnConfirmDeleteReviewButtonClick -> deleteUserReview()
            Action.OnConfirmReportReviewButtonClick -> Unit
        }
    }

    private fun updateReviewRating(rating: Int) {
        uiState = uiState.copy(
            userReviewState = uiState.userReviewState.copy(
                rating = rating,
                visible = true
            )
        )
    }

    private fun updateReviewTitle(title: String) {
        if (title.length > TITLE_MAX_LENGTH) return
        uiState = uiState.copy(userReviewState = uiState.userReviewState.copy(title = title))
    }


    private fun updateReviewDescription(description: String) {
        if (description.length > DESCRIPTION_MAX_LENGTH) return
        uiState = uiState.copy(
            userReviewState = uiState.userReviewState.copy(description = description),
        )
    }

    private fun getMoreReviews() {
        fetchReviewsJob?.cancel()
        fetchReviewsJob = viewModelScope.launch {
            getPlaceReviewsUseCase.getMoreReviews(placeId).collectLatest { status ->
                val reviewsState = uiState.reviewsState
                if (reviewsState !is ReviewsState.Success) return@collectLatest
                val state = when (status) {
                    is GetPlaceReviewsStatus.Exception -> reviewsState.copy(loadingMore = false)
                    GetPlaceReviewsStatus.Loading -> reviewsState.copy(loadingMore = true)
                    is GetPlaceReviewsStatus.Success -> {
                        val reviews = reviewsState.otherReviews + status.paginatedReviews
                        reviewsState.copy(
                            otherReviews = reviews,
                            hasMoreReviews = status.hasMoreReviews,
                            loadingMore = false,
                        )
                    }
                }
                uiState = uiState.copy(reviewsState = state)
            }
        }
    }

    private fun deleteUserReview() {
        deleteUserReviewJob?.cancel()
        deleteUserReviewJob = viewModelScope.launch {
            deleteUserReviewUseCase(placeId).collectLatest { status ->
                when (status) {
                    DeleteUserReviewStatus.Loading -> uiState =
                        uiState.copy(alertDialog = null, loading = true)
                    DeleteUserReviewStatus.Error -> uiState =
                        uiState.copy(alertDialog = AlertDialog.Error(), loading = false)
                    DeleteUserReviewStatus.Success -> uiState =
                        when (val reviewsState = uiState.reviewsState) {
                            is ReviewsState.Success -> {
                                uiState.copy(
                                    alertDialog = null,
                                    reviewsState = reviewsState.copy(userReview = null),
                                    loading = false,
                                )
                            }
                            else -> uiState.copy(alertDialog = AlertDialog.Error(), loading = false)
                        }
                }
            }
        }
    }

    private fun submitReview() {
        submitReviewJob?.cancel()
        submitReviewJob = viewModelScope.launch {
            submitReviewUseCase(
                placeId = placeId,
                rating = uiState.userReviewState.rating,
                title = uiState.userReviewState.title,
                description = uiState.userReviewState.description,
            ).collectLatest { status ->
                when (status) {
                    SubmitReviewStatus.Loading -> {
                        uiState = uiState.copy(
                            userReviewState = uiState.userReviewState.copy(loading = true),
                        )
                    }
                    is SubmitReviewStatus.Exception -> {
                        uiState = uiState.copy(
                            userReviewState = uiState.userReviewState.copy(loading = false),
                            alertDialog = AlertDialog.Error(
                                title = status.title,
                                message = status.message
                            ),
                        )
                    }
                    is SubmitReviewStatus.Success -> {
                        when (val reviewsState = uiState.reviewsState) {
                            is ReviewsState.Success -> {
                                uiState = uiState.copy(
                                    userReviewState = UserReviewState(),
                                    reviewsState = reviewsState.copy(
                                        userReview = status.reviewViewEntity,
                                    )
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    data class UiState(
        val detailsState: DetailsState = DetailsState.Loading,
        val reviewsState: ReviewsState = ReviewsState.Loading,
        val userReviewState: UserReviewState = UserReviewState(),
        val alertDialog: AlertDialog? = null,
        val loading: Boolean = false,
    )

    sealed class AlertDialog {
        data class Error(
            @StringRes val title: Int = R.string.error_unknown_failure_title,
            @StringRes val message: Int = R.string.error_unknown_failure_message,
        ) : AlertDialog()

        object DiscardReview : AlertDialog()
        object DeleteReview : AlertDialog()
        object ReportReview : AlertDialog()
    }

    sealed class DetailsState {
        object Loading : DetailsState()
        object Error : DetailsState()
        data class Success(val content: List<PlaceDetailsScreenItem>) : DetailsState()
    }

    data class UserReviewState(
        val rating: Int = 0,
        val title: String = "",
        val description: String = "",
        val loading: Boolean = false,
        val visible: Boolean = false,
    ) {
        val isSubmitButtonEnabled: Boolean = title.isNotBlank()
    }

    sealed class ReviewsState {
        object Loading : ReviewsState()
        data class Error(@StringRes val message: Int) : ReviewsState()
        data class Success(
            val userReview: ReviewViewEntity? = null,
            val otherReviews: List<ReviewViewEntity> = emptyList(),
            val loadingMore: Boolean = false,
            val hasMoreReviews: Boolean = true,
        ) : ReviewsState() {
            val isEmpty = userReview == null && otherReviews.isEmpty()
            val containsUserReview: Boolean = userReview != null
        }
    }

    sealed class Action {
        data class OnUserReviewRatingUpdate(val rating: Int) : Action()
        data class OnUserReviewTitleUpdate(val title: String) : Action()
        data class OnUserReviewDescriptionUpdate(val description: String) : Action()
        object OnReportPlaceClick : Action()
        object OnEditPlaceClick : Action()
        object OnAlertDialogDismissRequest : Action()
        object OnDiscardReviewIconClick : Action()
        object OnConfirmDiscardReviewButtonClick : Action()
        object OnGetMoreReviewsButtonClick : Action()
        object OnReportReviewIconClick : Action()
        object OnDeleteReviewIconClick : Action()
        object OnConfirmDeleteReviewButtonClick : Action()
        object OnConfirmReportReviewButtonClick : Action()
        object SubmitReview : Action()
    }

    sealed class SideEffect {

    }

    companion object {
        private const val TITLE_MAX_LENGTH = 80
        private const val DESCRIPTION_MAX_LENGTH = 600
    }
}