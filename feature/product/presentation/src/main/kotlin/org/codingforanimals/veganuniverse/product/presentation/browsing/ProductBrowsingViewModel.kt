package org.codingforanimals.veganuniverse.product.presentation.browsing

import androidx.annotation.StringRes
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
import org.codingforanimals.veganuniverse.product.domain.ProductRepository
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductQueryParams
import org.codingforanimals.veganuniverse.product.model.ProductSorter
import org.codingforanimals.veganuniverse.product.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.browsing.mapper.toView
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionType
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.presentation.toUI
import org.codingforanimals.veganuniverse.ui.dialog.Dialog

@OptIn(ExperimentalCoroutinesApi::class)
class ProductBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    productRepository: ProductRepository,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

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
            productRepository.queryProductsPagingDataFlow(params).map { pagingData ->
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
                    sideEffectsChannel.send(SideEffect.NavigateUp)
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

            is Action.ProductSuggestionDialogAction -> handleProductSuggestionDialogAction(action)
            is Action.RelayAction.NavigateToAuthScreen -> handleRelayActions(action)
        }
    }

    private suspend fun searchProducts() = searchChannel.send(Unit)

    private fun handleProductSuggestionDialogAction(action: Action.ProductSuggestionDialogAction) {
        _uiState.value = uiState.value.copy(
            productSuggestionType = when (action) {
                is Action.ProductSuggestionDialogAction.OpenEdit -> {
                    ProductSuggestionType.Edit(action.product)
                }

                is Action.ProductSuggestionDialogAction.OpenReport -> {
                    ProductSuggestionType.Report(action.product)
                }

                is Action.ProductSuggestionDialogAction.Close -> {
                    null
                }
            }
        )
    }

    private fun handleRelayActions(action: Action.RelayAction) {
        when (action) {
            Action.RelayAction.NavigateToAuthScreen -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToAuthScreen)
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
        val productSuggestionType: ProductSuggestionType? = null,
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

        sealed class ProductSuggestionDialogAction : Action() {
            data class OpenEdit(val product: Product) : ProductSuggestionDialogAction()
            data class OpenReport(val product: Product) : ProductSuggestionDialogAction()
            data object Close : ProductSuggestionDialogAction()
        }

        sealed class RelayAction : Action() {
            data object NavigateToAuthScreen : RelayAction()
        }
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data object NavigateToAuthScreen : SideEffect()
    }
}

const val CATEGORY_ARG = "category_arg"
const val TYPE_ARG = "type_arg"
const val SORTER_ARG = "sorter_arg"
