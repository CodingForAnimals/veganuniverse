package org.codingforanimals.veganuniverse.product.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts

internal class ProductHomeViewModel(
    val getLatestProducts: GetLatestProducts,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    val latestProductsState = flow {
        val result = runCatching {
            LatestProductsState.Success(getLatestProducts())
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
            LatestProductsState.Error
        }
        emit(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LatestProductsState.Loading,
    )

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
        }
    }

    private fun navigateToProductBrowsing(
        category: ProductCategory? = null,
        type: ProductType? = null,
        sorter: ProductSorter? = null,
    ) {
        val navigationEffect = NavigationEffect.NavigateToProductBrowsing(
            category = category,
            type = type,
            sorter = sorter
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
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnMostRecentShowMoreClick : Action()
        data object OnShowAllClick : Action()
        data class OnProductClick(val product: Product) : Action()
        data class OnProductTypeClick(val type: ProductType) : Action()
    }

    sealed class NavigationEffect {
        data class NavigateToProductBrowsing(
            val category: ProductCategory? = null,
            val type: ProductType? = null,
            val sorter: ProductSorter? = null,
        ) : NavigationEffect()

        data class NavigateToProductDetail(val id: String) : NavigationEffect()
    }

    companion object {
        private const val TAG = "ProductHomeViewModel"
    }
}