package org.codingforanimals.veganuniverse.product.presentation.list

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
import org.codingforanimals.veganuniverse.product.domain.usecase.GetPaginatedProducts
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionType
import org.codingforanimals.veganuniverse.product.presentation.list.mapper.toViewModel
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.dialog.Dialog

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModel(
    savedStateHandle: SavedStateHandle,
    getPaginatedProducts: GetPaginatedProducts,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

    private val _uiState = MutableStateFlow(UiState.fromCategory(savedStateHandle[CATEGORY_ARG]))
    val uiState = _uiState.asStateFlow()

    private val searchChannel = Channel<Unit>()
    val products: Flow<PagingData<Product>> =
        searchChannel.receiveAsFlow().flatMapLatest {
            getPaginatedProducts(
                uiState.value.searchText,
                uiState.value.category?.name,
                uiState.value.filterType?.name,
            ).map { it.map { product -> product.toViewModel() } }
                .cachedIn(viewModelScope)
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
                    val filtersChanged =
                        action.type != uiState.value.filterType || action.category != uiState.value.category
                    if (filtersChanged) {
                        _uiState.value = uiState.value.copy(
                            category = action.category,
                            filterType = action.type,
                        )
                        searchProducts()
                    }
                }
            }

            Action.OnClearFiltersClick -> {
                if (uiState.value.filterType == null && uiState.value.category == null) return
                _uiState.value = uiState.value.copy(
                    filterType = null,
                    category = null,
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
        val filterType: ProductType? = null,
        val dialog: Dialog? = null,
        val productSuggestionType: ProductSuggestionType? = null,
        val loading: Boolean = false,
    ) {
        companion object {
            fun fromCategory(categoryNavArg: String?): UiState {
                return categoryNavArg?.let {
                    UiState(category = ProductCategory.fromString(it))
                } ?: UiState()
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnClearFiltersClick : Action()

        data class OnSearchTextChange(val text: String) : Action()
        data class ApplyFiltersClick(val category: ProductCategory?, val type: ProductType?) :
            Action()

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

    companion object {
        const val CATEGORY_ARG = "category_arg"
    }
}
