package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.domain.usecase.GetLatestProducts
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionType
import org.codingforanimals.veganuniverse.product.presentation.list.mapper.toViewModel
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.ui.ProductCategory

class ProductHomeViewModel(
    getLatestProducts: GetLatestProducts,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    val latestProductsState = getLatestProducts().transform {
        emit(
            when (it) {
                GetLatestProducts.State.Error -> LatestProductsState.Error
                GetLatestProducts.State.Loading -> LatestProductsState.Loading
                is GetLatestProducts.State.Success -> LatestProductsState.Success(
                    it.products.map { domainModel -> domainModel.toViewModel() }
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LatestProductsState.Idle,
    )

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductCategorySelected -> navigateToCategoryList(action.category.name)
            Action.OnSeeAllClick -> navigateToCategoryList()
            Action.OnCreateProductClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToCreateProductScreen)
                }
            }

            is Action.ImageDialogAction -> handleImageDialogAction(action)
            is Action.ProductSuggestionDialogAction -> handleProductActionDialog(action)
            is Action.RelayAction.NavigateToAuthScreen -> handleRelayActions(action)
        }
    }

    private fun navigateToCategoryList(category: String? = null) {
        viewModelScope.launch {
            sideEffectsChannel.send(SideEffect.NavigateToCategoryListScreen(category))
        }
    }

    private fun handleImageDialogAction(action: Action.ImageDialogAction) {
        mutableUiState.value = uiState.value.copy(
            imageUrl = when (action) {
                Action.ImageDialogAction.Close -> null
                is Action.ImageDialogAction.Open -> action.url
            }
        )
    }

    private fun handleProductActionDialog(action: Action.ProductSuggestionDialogAction) {
        mutableUiState.value = uiState.value.copy(
            productActionDialog = when (action) {
                is Action.ProductSuggestionDialogAction.OpenEdit -> {
                    ProductSuggestionType.Edit(action.product)
                }

                is Action.ProductSuggestionDialogAction.OpenReport -> {
                    ProductSuggestionType.Report(action.product)
                }

                is Action.ProductSuggestionDialogAction.Dismiss -> {
                    if (action.snackbarMessage != null) {
                        viewModelScope.launch {
                            sideEffectsChannel.send(
                                SideEffect.ShowSnackbar(
                                    message = action.snackbarMessage,
                                    actionLabel = action.actionLabel,
                                    action = action.action,
                                )
                            )
                        }
                    }
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
        val categories: List<ProductCategory> = ProductCategory.values().asList(),
        val imageUrl: String? = null,
        val productActionDialog: ProductSuggestionType? = null,
    )

    sealed class LatestProductsState {
        data object Idle : LatestProductsState()
        data object Loading : LatestProductsState()
        data object Error : LatestProductsState()
        data class Success(val products: List<Product>) : LatestProductsState()
    }

    sealed class Action {
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnSeeAllClick : Action()
        data object OnCreateProductClick : Action()
        sealed class ImageDialogAction : Action() {
            data class Open(val url: String?) : ImageDialogAction()
            data object Close : ImageDialogAction()
        }

        sealed class ProductSuggestionDialogAction : Action() {
            data class OpenEdit(val product: Product) : ProductSuggestionDialogAction()
            data class OpenReport(val product: Product) : ProductSuggestionDialogAction()
            data class Dismiss(
                val snackbarMessage: Int?,
                val actionLabel: Int? = null,
                val action: (suspend () -> Unit)? = null,
            ) : ProductSuggestionDialogAction()
        }

        sealed class RelayAction : Action() {
            data object NavigateToAuthScreen : RelayAction()
        }
    }

    sealed class SideEffect {
        data class NavigateToCategoryListScreen(val categoryName: String?) : SideEffect()
        data object NavigateToCreateProductScreen : SideEffect()
        data object NavigateToAuthScreen : SideEffect()

        data class ShowSnackbar(
            @StringRes val message: Int,
            @StringRes val actionLabel: Int?,
            val action: (suspend () -> Unit)?,
        ) : SideEffect()
    }
}