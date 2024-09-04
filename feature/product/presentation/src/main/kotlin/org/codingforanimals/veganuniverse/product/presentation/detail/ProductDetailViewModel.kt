package org.codingforanimals.veganuniverse.product.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.edit_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.report_error
import org.codingforanimals.veganuniverse.commons.ui.R.string.report_success
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error_message_try_again
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductDetail
import org.codingforanimals.veganuniverse.product.domain.usecase.ProductBookmarkUseCases
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination

internal class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    getProductDetail: GetProductDetail,
    private val bookmarkUseCases: ProductBookmarkUseCases,
    private val reportProduct: ReportProduct,
    private val editProduct: EditProduct,
    private val verifiedOnlyUserAction: VerifiedOnlyUserAction,
) : ViewModel() {
    private val id = savedStateHandle.get<String>(ProductDestination.Detail.ID_ARG)

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    val product = flow {
        id ?: return@flow
        getProductDetail(id)?.let { result ->
            emit(State.Success(result.product))
        } ?: emit(State.Error)
    }.stateIn(
        scope = viewModelScope,
        initialValue = State.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    var dialog: Dialog? by mutableStateOf(null)
        private set

    private var bookmarkActionEnabled: Boolean = true
    private val bookmarkActionChannel = Channel<Boolean>()
    val isBookmarked = channelFlow {
        id ?: return@channelFlow
        send(bookmarkUseCases.isBookmarked(id))

        bookmarkActionChannel.receiveAsFlow().collectLatest { currentValue ->
            bookmarkActionEnabled = false
            send(!currentValue)
            val result = bookmarkUseCases.toggleBookmark(id, currentValue)
            bookmarkActionEnabled = true

            if (!result.isSuccess) {
                send(currentValue)
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = unexpected_error_message_try_again,
                    )
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = false,
    )

    fun onAction(action: Action) {
        when (action) {
            Action.OnBookmarkClick -> {
                if (bookmarkActionEnabled) {
                    viewModelScope.launch {
                        verifiedOnlyUserAction {
                            bookmarkActionChannel.send(isBookmarked.value)
                        }
                    }
                }
            }

            Action.OnReportClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        dialog = Dialog.Report
                    }
                }
            }

            Action.OnSuggestClick -> {
                viewModelScope.launch {
                    verifiedOnlyUserAction {
                        dialog = Dialog.Suggestion
                    }
                }
            }

            Action.OnDismissDialog -> {
                dialog = null
            }
        }
    }

    fun onReportResult(result: ReportContentDialogResult) {
        onAction(Action.OnDismissDialog)
        when (result) {
            ReportContentDialogResult.Dismiss -> Unit
            ReportContentDialogResult.SendReport -> {
                id ?: return
                viewModelScope.launch {
                    if (reportProduct(id).isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(report_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(report_error))
                    }
                }
            }
        }
    }

    fun onSuggestionResult(result: EditContentDialogResult) {
        onAction(Action.OnDismissDialog)
        when (result) {
            EditContentDialogResult.Dismiss -> Unit
            is EditContentDialogResult.SendEdit -> {
                id ?: return
                viewModelScope.launch {
                    if (editProduct(id, result.edition).isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(edit_success))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(edit_error))
                    }
                }
            }
        }
    }

    fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        onAction(Action.OnDismissDialog)
        when (result) {
            UnverifiedEmailResult.Dismissed -> Unit
            UnverifiedEmailResult.UnexpectedError -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(Snackbar(verification_email_not_sent))
                }
            }

            UnverifiedEmailResult.VerificationEmailSent -> {
                viewModelScope.launch {
                    snackbarEffectsChannel.send(Snackbar(verification_email_sent))
                }
            }
        }
    }

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class Success(val product: Product) : State()
    }

    sealed class Action {
        data object OnBookmarkClick : Action()
        data object OnSuggestClick : Action()
        data object OnReportClick : Action()
        data object OnDismissDialog : Action()
    }

    sealed class Dialog {
        data object Report : Dialog()
        data object Suggestion : Dialog()
        data object UnverifiedEmail : Dialog()
    }
}
