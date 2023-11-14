package org.codingforanimals.veganuniverse.places.presentation.details

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
import org.codingforanimals.veganuniverse.auth.model.User
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.places.presentation.R
import org.codingforanimals.veganuniverse.places.presentation.details.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.presentation.details.model.DeleteUserReviewStatus
import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceDetailsScreenItem
import org.codingforanimals.veganuniverse.places.presentation.details.model.SubmitReviewStatus
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.BookmarkPlaceUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.DeleteUserReviewUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceDetailsScreenContent
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceDetailsUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.GetPlaceReviewsUseCase
import org.codingforanimals.veganuniverse.places.presentation.details.usecase.SubmitReviewUseCase
import org.codingforanimals.veganuniverse.places.presentation.model.GetPlaceDetailsStatus
import org.codingforanimals.veganuniverse.places.presentation.model.GetPlaceReviewsStatus
import org.codingforanimals.veganuniverse.places.presentation.model.GetUserReviewStatus
import org.codingforanimals.veganuniverse.places.presentation.navigation.selected_place_id
import org.codingforanimals.veganuniverse.ui.icon.ToggleIconState

internal class PlaceDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    getUserStatus: GetUserStatus,
    private val getPlaceDetailsScreenContent: GetPlaceDetailsScreenContent,
    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase,
    private val getPlaceReviewsUseCase: GetPlaceReviewsUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val deleteUserReviewUseCase: DeleteUserReviewUseCase,
    private val bookmarkPlace: BookmarkPlaceUseCase,
) : ViewModel() {

    private var submitReviewJob: Job? = null
    private var getPlaceReviewsJob: Job? = null
    private var getMoreReviewsJob: Job? = null
    private var deleteUserReviewJob: Job? = null
    private var bookmarkJob: Job? = null

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    private val placeId = checkNotNull(savedStateHandle.get<String>(selected_place_id))

    init {
        viewModelScope.launch {
            getPlaceDetailsUseCase(placeId).collectLatest { status ->
                uiState = when (status) {
                    GetPlaceDetailsStatus.Loading -> {
                        uiState.copy(detailsState = DetailsState.Loading)
                    }

                    is GetPlaceDetailsStatus.Exception -> {
                        uiState.copy(detailsState = DetailsState.Error)
                    }

                    is GetPlaceDetailsStatus.Success -> {
                        val content = getPlaceDetailsScreenContent(status.place)
                        uiState.copy(detailsState = DetailsState.Success(content))
                    }
                }
            }
        }
        viewModelScope.launch {
            getPlaceReviewsJob = launch {
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
        viewModelScope.launch {
            getUserStatus().collectLatest { user ->
                uiState = uiState.copy(
                    user = user,
                    userReviewState = uiState.userReviewState.copy(username = user?.name)
                )
                getPlaceReviewsUseCase.getUserReview(placeId).collectLatest { status ->
                    when (status) {
                        is GetUserReviewStatus.Exception -> Unit
                        GetUserReviewStatus.Loading -> Unit
                        is GetUserReviewStatus.Success -> {
                            when (val state = uiState.reviewsState) {
                                is ReviewsState.Error -> Unit
                                ReviewsState.Loading -> uiState =
                                    uiState.copy(reviewsState = ReviewsState.Success(userReview = status.userReview))

                                is ReviewsState.Success -> uiState = uiState.copy(
                                    reviewsState = state.copy(
                                        userReview = status.userReview,
                                        otherReviews = state.otherReviews.filter { it.userId != user?.id },
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        viewModelScope.launch {
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
        viewModelScope.launch {
            bookmarkPlace.getCurrent(placeId).collectLatest { status ->
                val bookmarkState = when (status) {
                    BookmarkPlaceUseCase.Status.Error,
                    BookmarkPlaceUseCase.Status.GuestUser,
                    -> ToggleIconState(
                        loading = false,
                        toggled = false
                    )

                    BookmarkPlaceUseCase.Status.Loading -> ToggleIconState(
                        loading = true,
                    )

                    is BookmarkPlaceUseCase.Status.Success -> ToggleIconState(
                        loading = false,
                        toggled = status.toggled,
                    )
                }
                uiState = uiState.copy(bookmarkState = bookmarkState)
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
            is Action.OnDeleteReviewIconClick -> uiState =
                uiState.copy(alertDialog = AlertDialog.DeleteReview(action.review))

            Action.OnReportReviewIconClick -> uiState =
                uiState.copy(alertDialog = AlertDialog.ReportReview)

            is Action.OnConfirmDeleteReviewButtonClick -> deleteUserReview(action.review)
            Action.OnConfirmReportReviewButtonClick -> Unit
            Action.OnBookmarkClick -> {
                handleBookmarkClick()
            }
        }
    }

    private fun updateReviewRating(rating: Int) {
        uiState = uiState.copy(
            userReviewState = uiState.userReviewState.copy(
                rating = rating,
                visible = true
            )
        )
        if (uiState.user == null) {
            navigateToAuthenticateScreen()
        }
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
        getMoreReviewsJob?.cancel()
        getMoreReviewsJob = viewModelScope.launch {
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

    private fun deleteUserReview(review: PlaceReview) {
        deleteUserReviewJob?.cancel()
        deleteUserReviewJob = viewModelScope.launch {
            deleteUserReviewUseCase(placeId, review).collectLatest { status ->
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

    private fun navigateToAuthenticateScreen() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.NavigateToAuthenticateScreen)
        }
    }

    private fun submitReview() {
        if (uiState.user == null) {
            navigateToAuthenticateScreen()
        } else {
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
                                            userReview = status.placeReview,
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
    }

    private fun handleBookmarkClick() {
        bookmarkJob?.cancel()
        bookmarkJob = viewModelScope.launch {
            bookmarkPlace(placeId, !uiState.bookmarkState.toggled).collectLatest { status ->
                uiState = when (status) {
                    BookmarkPlaceUseCase.Status.Error ->
                        uiState.copy(
                            alertDialog = AlertDialog.Error(),
                            bookmarkState = uiState.bookmarkState.copy(loading = false)
                        )

                    BookmarkPlaceUseCase.Status.GuestUser -> {
                        _sideEffects.send(SideEffect.NavigateToAuthenticateScreen)
                        uiState.copy(
                            bookmarkState = uiState.bookmarkState.copy(loading = false)
                        )
                    }

                    BookmarkPlaceUseCase.Status.Loading ->
                        uiState.copy(
                            bookmarkState = uiState.bookmarkState.copy(loading = true)
                        )

                    is BookmarkPlaceUseCase.Status.Success -> uiState.copy(
                        bookmarkState = uiState.bookmarkState.copy(
                            toggled = status.toggled,
                            loading = false
                        )
                    )
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
        val user: User? = null,
        val bookmarkState: ToggleIconState = ToggleIconState(),
    )

    sealed class AlertDialog {
        data class Error(
            @StringRes val title: Int = R.string.error_unknown_failure_title,
            @StringRes val message: Int = R.string.error_unknown_failure_message,
        ) : AlertDialog()

        data object DiscardReview : AlertDialog()
        data class DeleteReview(val review: PlaceReview) : AlertDialog()
        data object ReportReview : AlertDialog()
    }

    sealed class DetailsState {
        data object Loading : DetailsState()
        data object Error : DetailsState()
        data class Success(val content: List<PlaceDetailsScreenItem>) : DetailsState()
    }

    data class UserReviewState(
        val rating: Int = 0,
        val username: String? = null,
        val title: String = "",
        val description: String = "",
        val loading: Boolean = false,
        val visible: Boolean = false,
    ) {
        val isSubmitButtonEnabled: Boolean = title.isNotBlank()
    }

    sealed class ReviewsState {
        data object Loading : ReviewsState()
        data class Error(@StringRes val message: Int) : ReviewsState()
        data class Success(
            val userReview: PlaceReview? = null,
            val otherReviews: List<PlaceReview> = emptyList(),
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
        data object OnReportPlaceClick : Action()
        data object OnEditPlaceClick : Action()
        data object OnAlertDialogDismissRequest : Action()
        data object OnDiscardReviewIconClick : Action()
        data object OnConfirmDiscardReviewButtonClick : Action()
        data object OnGetMoreReviewsButtonClick : Action()
        data object OnReportReviewIconClick : Action()
        data class OnDeleteReviewIconClick(val review: PlaceReview) : Action()
        data class OnConfirmDeleteReviewButtonClick(val review: PlaceReview) : Action()
        data object OnConfirmReportReviewButtonClick : Action()
        data object SubmitReview : Action()
        data object OnBookmarkClick : Action()
    }

    sealed class SideEffect {
        data object NavigateToAuthenticateScreen : SideEffect()
    }

    companion object {
        private const val TITLE_MAX_LENGTH = 80
        private const val DESCRIPTION_MAX_LENGTH = 600
    }
}