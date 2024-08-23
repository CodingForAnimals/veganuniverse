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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.product.presentation.label
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.product.domain.usecase.QueryProductsPagingDataFlow
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.toView
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.CATEGORY
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.SORTER
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.Browsing.Companion.TYPE

internal class ProductBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    queryProductsPagingDataFlow: QueryProductsPagingDataFlow,
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
        )
    )
        private set

    private val searchChannel = Channel<Unit>()
    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> =
        searchChannel.receiveAsFlow().flatMapLatest {
            val params = ProductQueryParams.Builder()
                .withKeyword(uiState.searchText)
                .withCategory(uiState.category)
                .withType(uiState.type)
                .withSorter(uiState.sorter)
                .build()
            queryProductsPagingDataFlow(params)
                .map { pagingData ->
                    pagingData.map { model -> model.toView() }
                }
        }.cachedIn(viewModelScope)

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
        val topBarLabel: Int = type?.toUI()?.label ?: run {
            when (sorter) {
                ProductSorter.NAME -> R.string.all_products
                ProductSorter.DATE -> sorter.label
            }
        }

        companion object {
            fun fromNavArgs(
                categoryNavArg: String?,
                typeNavArg: String?,
                sorterNavArg: String?,
            ): UiState {
                return categoryNavArg?.let {
                    UiState(
                        category = ProductCategory.fromString(it),
                        type = typeNavArg?.let { ProductType.fromString(it) },
                        sorter = sorterNavArg?.let { ProductSorter.fromString(it) }
                            ?: ProductSorter.NAME,
                    )
                } ?: UiState()
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
        data object NavigateToAuthScreen : NavigationEffect()
        data class NavigateToProductDetails(val id: String) : NavigationEffect()
    }
}

