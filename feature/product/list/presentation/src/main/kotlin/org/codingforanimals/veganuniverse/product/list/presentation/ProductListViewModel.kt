package org.codingforanimals.veganuniverse.product.list.presentation

import android.util.Log
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
import org.codingforanimals.veganuniverse.product.list.domain.usecase.UseCases
import org.codingforanimals.veganuniverse.product.list.presentation.mapper.toViewModel
import org.codingforanimals.veganuniverse.product.list.presentation.model.Product
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.dialog.Dialog

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModel(
    savedStateHandle: SavedStateHandle,
    useCases: UseCases,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

    var uiState by mutableStateOf(UiState.init(savedStateHandle.get<String>(CATEGORY_ARG)))
        private set

    private val searchChannel = Channel<Unit>()
    val products: Flow<PagingData<Product>> =
        searchChannel.receiveAsFlow().flatMapLatest {
            useCases.getProducts(
                uiState.searchText,
                uiState.category?.name,
                uiState.filterType?.name,
            ).map { it.map { product -> product.toViewModel() } }
                .cachedIn(viewModelScope)
        }

    private suspend fun searchProducts() = searchChannel.send(Unit)

    init {
        viewModelScope.launch {
            searchChannel.send(Unit)
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
                    val filtersChanged =
                        action.type != uiState.filterType || action.category != uiState.category
                    if (filtersChanged) {
                        uiState = uiState.copy(
                            category = action.category,
                            filterType = action.type,
                        )
                        searchProducts()
                    }
                }
            }

            Action.OnClearFiltersClick -> {
                if (uiState.filterType == null && uiState.category == null) return
                uiState = uiState.copy(
                    filterType = null,
                    category = null,
                )
                viewModelScope.launch {
                    searchProducts()
                }
            }
        }
    }

    data class UiState(
        val category: ProductCategory?,
        val searchText: String = "",
        val filterType: ProductType? = null,
        val dialog: Dialog? = null,
        val loading: Boolean = false,
    ) {
        companion object {
            fun init(categoryNavArg: String?): UiState {
                return try {
                    val category = ProductCategory.valueOf(categoryNavArg!!)
                    UiState(category)
                } catch (e: Throwable) {
                    Log.e(TAG, e.stackTraceToString())
                    UiState(
                        category = ProductCategory.BEVERAGES,
                        dialog = Dialog.unknownErrorDialog(),
                    )
                }
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object OnClearFiltersClick : Action()
        data class OnSearchTextChange(val text: String) : Action()
        data class ApplyFiltersClick(val category: ProductCategory?, val type: ProductType?) :
            Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
    }

    companion object {
        private const val TAG = "ProductListViewModel"
        const val CATEGORY_ARG = "category_arg"
    }
}