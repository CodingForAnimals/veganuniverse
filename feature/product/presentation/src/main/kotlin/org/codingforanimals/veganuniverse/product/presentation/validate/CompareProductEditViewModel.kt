package org.codingforanimals.veganuniverse.product.presentation.validate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination

internal class CompareProductEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
) : ViewModel() {
    private val editId: String = savedStateHandle[ProductDestination.CompareEdit.EDIT_ID] ?: ""
    private val originalId: String =
        savedStateHandle[ProductDestination.CompareEdit.ORIGINAL_ID] ?: ""

    val state = flow<State> {
        emit(
            State.Success(
                edit = productRepository.getProductEditByIdFromRemote(editId),
                original = productRepository.getValidatedProductByIdFromRemote(originalId),
            )
        )
    }.catch {
        emit(State.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Loading,
    )

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class Success(val edit: ProductEdit, val original: Product) : State()
    }
}
