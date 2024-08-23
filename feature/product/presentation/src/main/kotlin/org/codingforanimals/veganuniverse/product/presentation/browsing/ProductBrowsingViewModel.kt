package org.codingforanimals.veganuniverse.product.presentation.browsing

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialogResult
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_not_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.R.string.verification_email_sent
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailResult
import org.codingforanimals.veganuniverse.product.domain.usecase.EditProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.QueryProductsPagingDataFlow
import org.codingforanimals.veganuniverse.product.domain.usecase.ReportProduct
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.browsing.mapper.toView
import org.codingforanimals.veganuniverse.product.presentation.model.Product

data class ProductBrowsingUseCases(
    val queryProductsPagingDataFlow: QueryProductsPagingDataFlow,
    val reportProduct: ReportProduct,
    val editProduct: EditProduct,
)

@OptIn(ExperimentalCoroutinesApi::class)
class ProductBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    private val useCases: ProductBrowsingUseCases,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

    var showReportDialog: String? by mutableStateOf(null)
        private set

    var showSuggestionDialog: String? by mutableStateOf(null)
        private set

    var showUnverifiedEmailDialog: Boolean by mutableStateOf(false)
        private set

    private val _uiState = MutableStateFlow(
        UiState.fromNavArgs(
            categoryNavArg = savedStateHandle[CATEGORY_ARG],
            typeNavArg = savedStateHandle[TYPE_ARG],
            sorterNavArg = savedStateHandle[SORTER_ARG],
        )
    )
    val uiState = _uiState.asStateFlow()

    private val searchChannel = Channel<Unit>()
    val products: Flow<PagingData<Product>> =
        searchChannel.receiveAsFlow().flatMapLatest {
            val params = ProductQueryParams.Builder()
                .withKeyword(uiState.value.searchText)
                .withCategory(uiState.value.category)
                .withType(uiState.value.type)
                .withSorter(uiState.value.sorter)
                .build()
            useCases.queryProductsPagingDataFlow(params).map { pagingData ->
                pagingData.map { model -> model.toView() }
            }.cachedIn(viewModelScope)
        }

    init {
        viewModelScope.launch {
            searchProducts()
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            is Action.OnSearchTextChange -> {
                _uiState.value = uiState.value.copy(searchText = action.text)
                searchTextDelayJob?.cancel()
                if (action.text.isEmpty() || action.text.length >= 3) {
                    searchTextDelayJob = viewModelScope.launch {
                        delay(searchTextDelayMs)
                        searchProducts()
                    }
                }
            }

            is Action.ApplyFiltersClick -> {
                viewModelScope.launch {
                    _uiState.value = uiState.value.copy(
                        category = action.category,
                        type = action.type,
                        sorter = action.sorter,
                    )
                    searchProducts()
                }
            }

            Action.OnClearFiltersClick -> {
                _uiState.value = uiState.value.copy(
                    type = null,
                    category = null,
                    sorter = ProductSorter.NAME,
                )
                viewModelScope.launch {
                    searchProducts()
                }
            }

            is Action.RelayAction.NavigateToAuthScreen -> {
                handleRelayActions(action)
            }

            is Action.OpenReportDialog -> {
                showReportDialog = action.productId
            }

            is Action.OpenSuggestDialog -> {
                showSuggestionDialog = action.productId
            }
        }
    }

    fun onReportResult(action: ReportContentDialogResult) {
        when (action) {
            ReportContentDialogResult.Dismiss -> {
                showReportDialog = null
            }

            ReportContentDialogResult.SendReport -> {
                viewModelScope.launch {
                    showReportDialog?.let {
                        val result = useCases.reportProduct(it)
                        showReportDialog = null
                        when (result) {
                            ReportProduct.Result.UnauthenticatedUser -> {
                                navigationEffectsChannel.send(NavigationEffect.NavigateToAuthScreen)
                            }

                            ReportProduct.Result.Success -> {
                                snackbarEffectsChannel.send(
                                    Snackbar(R.string.product_report_success)
                                )
                            }

                            ReportProduct.Result.UnexpectedError -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_report_error))
                            }

                            ReportProduct.Result.UnverifiedEmail -> {
                                showUnverifiedEmailDialog = true
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEditResult(action: EditContentDialogResult) {
        when (action) {
            EditContentDialogResult.Dismiss -> {
                showSuggestionDialog = null
            }

            is EditContentDialogResult.SendEdit -> {
                viewModelScope.launch {
                    showSuggestionDialog?.let {
                        val result = useCases.editProduct(it, action.edition)
                        showSuggestionDialog = null
                        when (result) {
                            EditProduct.Result.UnauthenticatedUser -> {
                                navigationEffectsChannel.send(NavigationEffect.NavigateToAuthScreen)
                            }

                            EditProduct.Result.UnverifiedEmail -> {
                                showUnverifiedEmailDialog = true
                            }

                            EditProduct.Result.UnexpectedError -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_edit_error))
                            }

                            EditProduct.Result.Success -> {
                                snackbarEffectsChannel.send(Snackbar(R.string.product_edit_success))
                            }
                        }
                    }
                }
            }
        }
    }

    fun onUnverifiedEmailResult(result: UnverifiedEmailResult) {
        showUnverifiedEmailDialog = false
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

    private suspend fun searchProducts() = searchChannel.send(Unit)

    private fun handleRelayActions(action: Action.RelayAction) {
        when (action) {
            Action.RelayAction.NavigateToAuthScreen -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateToAuthScreen)
                }
            }
        }
    }

    data class UiState(
        val category: ProductCategory? = null,
        val searchText: String = "",
        val type: ProductType? = null,
        val sorter: ProductSorter = ProductSorter.NAME,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
    ) {

        @StringRes
        val topBarLabel: Int = type?.toUI()?.label ?: R.string.all_products

        companion object {
            fun fromNavArgs(
                categoryNavArg: String?,
                typeNavArg: String?,
                sorterNavArg: String?,
            ): UiState {
                return categoryNavArg?.let {
                    UiState(
                        category = ProductCategory.fromString(it),
                        type = ProductType.fromString(typeNavArg),
                        sorter = ProductSorter.fromString(sorterNavArg) ?: ProductSorter.NAME,
                    )
                } ?: UiState()
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnClearFiltersClick : Action()

        data class OnSearchTextChange(val text: String) : Action()
        data class ApplyFiltersClick(
            val category: ProductCategory?,
            val type: ProductType?,
            val sorter: ProductSorter
        ) : Action()

        sealed class RelayAction : Action() {
            data object NavigateToAuthScreen : RelayAction()
        }

        data class OpenReportDialog(val productId: String) : Action()
        data class OpenSuggestDialog(val productId: String) : Action()
    }

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data object NavigateToAuthScreen : NavigationEffect()
    }
}

const val CATEGORY_ARG = "category_arg"
const val TYPE_ARG = "type_arg"
const val SORTER_ARG = "sorter_arg"
