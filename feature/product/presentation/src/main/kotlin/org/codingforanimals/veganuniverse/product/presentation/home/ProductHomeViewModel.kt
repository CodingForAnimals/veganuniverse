package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductSorter
import org.codingforanimals.veganuniverse.product.domain.model.ProductType

internal class ProductHomeViewModel : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    var searchText by mutableStateOf("")
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductCategorySelected -> {
                navigateToProductBrowsing(category = action.category)
            }

            Action.OnMostRecentShowMoreClick -> {
                navigateToProductBrowsing(sorter = ProductSorter.DATE)
            }

            is Action.OnProductClick -> {
                action.product.id?.let { id ->
                    viewModelScope.launch {
                        navigationEffectsChannel.send(NavigationEffect.NavigateToProductDetail(id))
                    }
                }
            }

            is Action.OnProductTypeClick -> {
                navigateToProductBrowsing(
                    type = action.type
                )
            }

            Action.OnShowAllClick -> {
                navigateToProductBrowsing()
            }

            is Action.OnSearchTextChange -> {
                searchText = action.text
            }

            Action.OnSearchTextAction -> {
                navigateToProductBrowsing(searchText = searchText)
            }

            Action.OnSeeAdditivesClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateToAdditives)
                }
            }
        }
    }

    private fun navigateToProductBrowsing(
        category: ProductCategory? = null,
        type: ProductType? = null,
        sorter: ProductSorter? = null,
        searchText: String? = null,
    ) {
        val navigationEffect = NavigationEffect.NavigateToProductBrowsing(
            category = category,
            type = type,
            sorter = sorter,
            searchText = searchText,
        )
        viewModelScope.launch {
            navigationEffectsChannel.send(navigationEffect)
        }
    }

    sealed class LatestProductsState {
        data object Loading : LatestProductsState()
        data object Error : LatestProductsState()
        data class Success(val products: List<Product>) : LatestProductsState()
    }

    sealed class Action {
        data object OnSearchTextAction : Action()
        data class OnSearchTextChange(val text: String) : Action()
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnMostRecentShowMoreClick : Action()
        data object OnShowAllClick : Action()
        data object OnSeeAdditivesClick : Action()

        data class OnProductClick(val product: Product) : Action()
        data class OnProductTypeClick(val type: ProductType) : Action()
    }

    sealed class NavigationEffect {
        data class NavigateToProductBrowsing(
            val category: ProductCategory? = null,
            val type: ProductType? = null,
            val sorter: ProductSorter? = null,
            val searchText: String? = null,
        ) : NavigationEffect()

        data class NavigateToProductDetail(val id: String) : NavigationEffect()
        data object NavigateToAdditives : NavigationEffect()
    }

    companion object {
        private const val TAG = "ProductHomeViewModel"
    }
}