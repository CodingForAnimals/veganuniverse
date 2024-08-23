package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.domain.ProductRepository
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductQueryParams
import org.codingforanimals.veganuniverse.product.model.ProductSorter
import org.codingforanimals.veganuniverse.product.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.browsing.mapper.toView
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionType
import org.codingforanimals.veganuniverse.product.presentation.model.Product

class ProductHomeViewModel(
    productRepository: ProductRepository,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    val latestProductsState = flow {
        val params = ProductQueryParams.Builder()
            .withSorter(ProductSorter.DATE)
            .withMaxSize(3)
            .withPageSize(3)
            .build()
        val result = runCatching {
            productRepository.queryProducts(params).map { it.toView() }
        }.getOrNull()?.let {
            LatestProductsState.Success(it)
        } ?: LatestProductsState.Error
        emit(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LatestProductsState.Loading,
    )

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.OnProductCategorySelected -> {
                navigateToProductBrowsing(category = action.category)
            }

            Action.OnMostRecentShowMoreClick -> {
                navigateToProductBrowsing(sorter = ProductSorter.DATE)
            }

            Action.OnCreateProductClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToCreateProduct)
                }
            }

            is Action.ImageDialogAction -> {
                handleImageDialogAction(action)
            }

            is Action.ProductSuggestionDialogAction -> {
                handleProductActionDialog(action)
            }

            is Action.RelayAction.NavigateToAuthScreen -> {
                handleRelayActions(action)
            }
        }
    }

    private fun navigateToProductBrowsing(
        category: ProductCategory? = null,
        type: ProductType? = null,
        sorter: ProductSorter? = null,
    ) {
        val sideEffect = SideEffect.NavigateToProductBrowsing(
            category = category,
            type = type,
            sorter = sorter
        )
        viewModelScope.launch {
            sideEffectsChannel.send(sideEffect)
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
                    sideEffectsChannel.send(SideEffect.NavigateToAuthentication)
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
        data object Loading : LatestProductsState()
        data object Error : LatestProductsState()
        data class Success(val products: List<Product>) : LatestProductsState()
    }

    sealed class Action {
        data class OnProductCategorySelected(val category: ProductCategory) : Action()
        data object OnMostRecentShowMoreClick : Action()
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
        data class NavigateToProductBrowsing(
            val category: ProductCategory? = null,
            val type: ProductType? = null,
            val sorter: ProductSorter? = null,
        ) : SideEffect()

        data object NavigateToCreateProduct : SideEffect()
        data object NavigateToAuthentication : SideEffect()

        data class ShowSnackbar(
            @StringRes val message: Int,
            @StringRes val actionLabel: Int?,
            val action: (suspend () -> Unit)?,
        ) : SideEffect()
    }
}