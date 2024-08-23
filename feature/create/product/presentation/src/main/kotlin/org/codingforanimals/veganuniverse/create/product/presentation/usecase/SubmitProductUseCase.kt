package org.codingforanimals.veganuniverse.create.product.presentation.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.create.product.domain.ProductCreator
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductViewModel
import org.codingforanimals.veganuniverse.product.entity.Product

private const val TAG = "SubmitProductUseCase"

class SubmitProductUseCase(
    private val getUserStatus: GetUserStatus,
    private val productCreator: ProductCreator,
) {
    suspend operator fun invoke(uiState: CreateProductViewModel.UiState): Flow<Status> = flow {
        try {
            val user = getUserStatus().firstOrNull() ?: return@flow emit(Status.GuestUser)
            if (!uiState.isFormValid()) {
                return@flow emit(Status.InvalidForm)
            }

            emit(Status.Loading)
            val comments = uiState.commentsField.value.ifBlank { null }
            val type = uiState.productTypeField.type?.name ?: return@flow emit(Status.InvalidForm)
            val category =
                uiState.productCategoryField.category?.name ?: return@flow emit(Status.InvalidForm)
            val product = Product(
                name = uiState.nameField.value,
                brand = uiState.brandField.value,
                comments = comments,
                type = type,
                category = category,
                userId = user.id,
                username = user.name,
            )
            productCreator.submitProduct(product)
            emit(Status.Success)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            emit(Status.Error)
        }
    }

    sealed class Status {
        data object Error : Status()
        data object InvalidForm : Status()
        data object Loading : Status()
        data object GuestUser : Status()
        data object Success : Status()
    }
}