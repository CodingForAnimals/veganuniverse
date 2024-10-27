package org.codingforanimals.veganuniverse.product.presentation.browsing

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductSorter
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toUI
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.CATEGORY
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.SEARCH_TEXT
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.SORTER
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.TYPE

internal class ProductBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    productRepository: ProductRepository,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

    var uiState by mutableStateOf(
        UiState.fromNavArgs(
            categoryNavArg = savedStateHandle[CATEGORY],
            typeNavArg = savedStateHandle[TYPE],
            sorterNavArg = savedStateHandle[SORTER],
            searchTextNavArg = savedStateHandle[SEARCH_TEXT],
        )
    )
        private set

    private val searchChannel = Channel<Unit>()

    val productsState: StateFlow<ProductsState> = searchChannel.receiveAsFlow().map {
        val products = productRepository.queryProductsFromLocal(
            query = uiState.searchText,
            type = uiState.type?.name,
            category = uiState.category?.name,
        )
        if (products.isEmpty()) {
            ProductsState.Empty
        } else {
            ProductsState.Success(products)
        }
    }.catch {
        emit(ProductsState.Error)
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = ProductsState.Loading,
    )

    sealed class ProductsState {
        data object Loading : ProductsState()
        data object Error : ProductsState()
        data object Empty : ProductsState()
        data class Success(val products: List<org.codingforanimals.veganuniverse.product.domain.model.Product>) :
            ProductsState()
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
                uiState = uiState.copy(searchText = action.text)
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
                    uiState = uiState.copy(
                        category = action.category,
                        type = action.type,
                        sorter = action.sorter,
                    )
                    searchProducts()
                }
            }

            Action.OnClearFiltersClick -> {
                uiState = uiState.copy(
                    type = null,
                    category = null,
                    sorter = ProductSorter.NAME,
                )
                viewModelScope.launch {
                    searchProducts()
                }
            }

            is Action.OnProductClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateToProductDetails(action.id))
                }
            }
        }
    }

    private suspend fun searchProducts() = searchChannel.send(Unit)

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
                searchTextNavArg: String?,
            ): UiState {
                return UiState(
                    category = ProductCategory.fromString(categoryNavArg),
                    type = typeNavArg?.let { ProductType.fromString(it) },
                    sorter = sorterNavArg?.let { ProductSorter.fromString(it) }
                        ?: ProductSorter.NAME,
                    searchText = searchTextNavArg.orEmpty()
                )
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnClearFiltersClick : Action()

        data class OnSearchTextChange(val text: String) : Action()
        data class OnProductClick(val id: String) : Action()
        data class ApplyFiltersClick(
            val category: ProductCategory?,
            val type: ProductType?,
            val sorter: ProductSorter
        ) : Action()
    }

    sealed class NavigationEffect {
        data object NavigateUp : NavigationEffect()
        data class NavigateToProductDetails(val id: String) : NavigationEffect()
    }
}

