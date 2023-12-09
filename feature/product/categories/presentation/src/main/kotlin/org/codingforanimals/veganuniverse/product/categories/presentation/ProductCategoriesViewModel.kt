package org.codingforanimals.veganuniverse.product.categories.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.ui.ProductCategory

class ProductCategoriesViewModel : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductCategorySelected -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToCategoryListScreen(action.category.name))
                }
            }
        }
    }

    data class UiState(
        val categories: List<ProductCategory> = ProductCategory.values().asList(),
    )

    sealed class Action {
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
    }

    sealed class SideEffect {
        data class NavigateToCategoryListScreen(val categoryName: String) : SideEffect()
    }
}