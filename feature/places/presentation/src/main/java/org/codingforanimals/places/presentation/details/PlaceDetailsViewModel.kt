package org.codingforanimals.places.presentation.details

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.places.presentation.details.model.SubmitReviewStatus
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsScreenContent
import org.codingforanimals.places.presentation.details.usecase.GetPlaceDetailsUseCase
import org.codingforanimals.places.presentation.details.usecase.GetPlaceReviewsUseCase
import org.codingforanimals.places.presentation.details.usecase.SubmitReviewUseCase
import org.codingforanimals.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.places.presentation.model.PlaceViewEntity
import org.codingforanimals.places.presentation.model.ReviewViewEntity
import org.codingforanimals.places.presentation.navigation.selected_place_id

internal class PlaceDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPlaceDetailsScreenContent: GetPlaceDetailsScreenContent,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase,
    private val getPlaceReviewsUseCase: GetPlaceReviewsUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
) : ViewModel() {

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    private val placeId = checkNotNull(savedStateHandle.get<String>(selected_place_id))
    private lateinit var place: PlaceViewEntity

    init {
        viewModelScope.launch {
            getPlaceDetailsUseCase(placeId).collectLatest { status ->
                when (status) {
                    GetPlaceDetailsStatus.Loading -> {
                        uiState = uiState.copy(detailsState = DetailsState.Loading)
                    }
                    is GetPlaceDetailsStatus.Exception -> {
                        uiState = uiState.copy(
                            alertDialog = AlertDialog.Error(
                                title = status.title,
                                message = status.message
                            )
                        )
                    }
                    is GetPlaceDetailsStatus.Success -> {
                        place = status.place
                        val content = getPlaceDetailsScreenContent(status.place, status.userReview)
                        uiState = uiState.copy(detailsState = DetailsState.Success(content))
                        fetchReviews()
                    }
                }
            }
        }
    }

    private fun fetchReviews() {
        viewModelScope.launch {
            getPlaceReviewsUseCase(placeId).collectLatest { status ->
                val state = when (status) {
                    GetPlaceReviewsStatus.Loading -> ReviewsState.Loading
                    GetPlaceReviewsStatus.Exception -> ReviewsState.Error
                    is GetPlaceReviewsStatus.Success -> {
                        when (val reviewsState = uiState.reviewsState) {
                            is ReviewsState.Success -> reviewsState.copy(otherReviews = status.reviews)
                            else -> ReviewsState.Success(otherReviews = status.reviews)
                        }
                    }
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
                alertDialog = AlertDialog.None,
                userReviewState = UserReviewState(),
            )
            is Action.OnUserReviewDescriptionUpdate -> updateReviewDescription(action.description)
            is Action.OnUserReviewRatingUpdate -> updateReviewRating(action.rating)
            is Action.OnUserReviewTitleUpdate -> updateReviewTitle(action.title)
            Action.OnAlertDialogDismissRequest -> uiState =
                uiState.copy(alertDialog = AlertDialog.None)
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

    private fun submitReview() {
        viewModelScope.launch {
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
                        uiState.detailsState.removeUserReviewItem()?.let { newDetailsState ->
                            val reviewsState = when (val reviewsState = uiState.reviewsState) {
                                is ReviewsState.Success -> reviewsState.copy(userReview = status.reviewViewEntity)
                                else -> ReviewsState.Success(userReview = status.reviewViewEntity)
                            }
                            uiState = uiState.copy(
                                userReviewState = UserReviewState(),
                                detailsState = newDetailsState,
                                reviewsState = reviewsState
                            )
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
        val alertDialog: AlertDialog = AlertDialog.None,
    )

    sealed class AlertDialog {
        data class Error(
            @StringRes val title: Int,
            @StringRes val message: Int,
        ) : AlertDialog()

        object DiscardReview : AlertDialog()
        object None : AlertDialog()
    }

    sealed class DetailsState {
        object Loading : DetailsState()
        data class Success(val content: List<PlaceDetailsScreenItem>) : DetailsState()

        fun removeUserReviewItem(): Success? {
            return when (this) {
                Loading -> null
                is Success -> {
                    val content = content.toMutableList()
                    content.remove(PlaceDetailsScreenItem.UserReview)
                    Success(content)
                }
            }
        }
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
        object Error : ReviewsState()
        data class Success(
            private val userReview: ReviewViewEntity? = null,
            private val otherReviews: List<ReviewViewEntity> = emptyList(),
        ) : ReviewsState() {

            val reviews: List<ReviewViewEntity> = if (userReview != null) {
                listOf(userReview, *otherReviews.toTypedArray())
            } else {
                otherReviews
            }

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
        object SubmitReview : Action()
    }

    sealed class SideEffect {

    }

    companion object {
        private const val TITLE_MAX_LENGTH = 80
        private const val DESCRIPTION_MAX_LENGTH = 600
    }
}