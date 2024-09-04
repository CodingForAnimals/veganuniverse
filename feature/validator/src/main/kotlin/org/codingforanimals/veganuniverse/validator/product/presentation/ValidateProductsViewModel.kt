package org.codingforanimals.veganuniverse.validator.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.validator.R
import org.codingforanimals.veganuniverse.validator.product.domain.GetUnvalidatedProductsPaginationFlowUseCase
import org.codingforanimals.veganuniverse.validator.product.domain.ValidateProductUseCase

internal class ValidateProductsViewModel(
    getUnvalidatedProductsPaginationFlowUseCase: GetUnvalidatedProductsPaginationFlowUseCase,
    private val validateProductUseCase: ValidateProductUseCase,
    private val deeplinkNavigator: DeeplinkNavigator,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    sealed class SideEffect {
        data object Refresh : SideEffect()
    }

    val unvalidatedProducts = getUnvalidatedProductsPaginationFlowUseCase().cachedIn(viewModelScope)

    fun validateProduct(product: Product) {
        viewModelScope.launch {
            if (validateProductUseCase(product.id ?: return@launch).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_validated_success)) }
                launch { sideEffectsChannel.send(SideEffect.Refresh) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_validated_error)) }
            }
        }
    }

    fun onProductClick(product: Product) {
        viewModelScope.launch {
            deeplinkNavigator.navigate(DeepLink.ProductDetail(product.id ?: return@launch))
        }
    }
}
