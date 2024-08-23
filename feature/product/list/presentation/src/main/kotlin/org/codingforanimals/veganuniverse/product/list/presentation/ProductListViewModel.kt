package org.codingforanimals.veganuniverse.product.list.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.ui.dialog.Dialog

class GetProductsUseCase

class ProductListViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState.init(savedStateHandle.get<String>(CATEGORY_ARG)))
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }
        }
    }

    data class UiState(
        val category: ProductCategory,
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
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
    }

    companion object {
        private const val TAG = "ProductListViewModel"
        const val CATEGORY_ARG = "category_arg"
    }
}