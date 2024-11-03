package org.codingforanimals.veganuniverse.product.presentation.validate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.usecase.DeleteProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.DeleteProductEdit
import org.codingforanimals.veganuniverse.product.domain.usecase.GetProductEdits
import org.codingforanimals.veganuniverse.product.domain.usecase.GetUnvalidatedProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateAllProducts
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateProduct
import org.codingforanimals.veganuniverse.product.domain.usecase.ValidateProductEdit
import org.codingforanimals.veganuniverse.product.presentation.R

internal class ValidateProductsViewModel(
    getUnvalidatedProducts: GetUnvalidatedProducts,
    getProductEdits: GetProductEdits,
    private val validateProduct: ValidateProduct,
    private val validateAllProducts: ValidateAllProducts,
    private val validateProductEdit: ValidateProductEdit,
    private val deleteProduct: DeleteProduct,
    private val deleteProductEdit: DeleteProductEdit,
) : ViewModel() {

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    private val refreshChannel = Channel<Unit>()
    val state = flow<State> {
        emit(State.Success(getUnvalidatedProducts(), getProductEdits()))
        refreshChannel.receiveAsFlow().collect {
            emit(State.Success(getUnvalidatedProducts(), getProductEdits()))
        }
    }.catch {
        emit(State.Error)
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Lazily,
        initialValue = State.Loading
    )

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class Success(val unvalidated: List<Product>, val edits: List<ProductEdit>) : State()
    }

    fun onConfirmValidateProduct(product: Product) {
        viewModelScope.launch {
            if (validateProduct(product).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_validated_success)) }
                launch { refreshChannel.send(Unit) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_validated_error)) }
            }
        }
    }

    fun onConfirmValidateProductEdit(edit: ProductEdit) {
        viewModelScope.launch {
            validateProductEdit(edit)
                .onSuccess {
                    launch { snackbarEffectsChannel.send(Snackbar(R.string.product_edit_validated_success)) }
                    launch { refreshChannel.send(Unit) }
                }
                .onFailure {
                    launch { snackbarEffectsChannel.send(Snackbar(R.string.product_edit_validated_error)) }
                }
        }
    }

    fun onConfirmValidateAll() {
        val (products, edits) = (state.value as? State.Success)?.let {
            Pair(it.unvalidated, it.edits)
        } ?: return
        viewModelScope.launch {
            if (validateAllProducts(products, edits).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.products_validated_success)) }
                launch { refreshChannel.send(Unit) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.products_validated_error)) }
                launch { refreshChannel.send(Unit) }
            }
        }
    }

    fun onConfirmDeleteEdit(edit: ProductEdit) {
        viewModelScope.launch {
            if (deleteProductEdit(edit).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_edit_deleted_success)) }
                launch { refreshChannel.send(Unit) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_edit_deleted_error)) }
                launch { refreshChannel.send(Unit) }
            }
        }
    }

    fun onConfirmDeleteProduct(product: Product) {
        viewModelScope.launch {
            if (deleteProduct(product).isSuccess) {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_deleted_success)) }
                launch { refreshChannel.send(Unit) }
            } else {
                launch { snackbarEffectsChannel.send(Snackbar(R.string.product_deleted_error)) }
                launch { refreshChannel.send(Unit) }
            }
        }
    }
}
