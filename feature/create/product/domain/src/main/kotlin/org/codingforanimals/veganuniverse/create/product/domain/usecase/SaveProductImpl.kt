package org.codingforanimals.veganuniverse.create.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus
import org.codingforanimals.veganuniverse.create.product.data.source.SaveProductRemoteDataSource
import org.codingforanimals.veganuniverse.create.product.data.model.SaveProductResult
import org.codingforanimals.veganuniverse.create.product.domain.mapper.toEntity
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm

internal class SaveProductImpl(
    private val remoteDataSource: SaveProductRemoteDataSource,
    private val getUserStatus: GetUserStatus,
) : SaveProduct {
    override suspend fun invoke(productForm: ProductForm): Flow<SaveProductStatus> = flow {
        val user = getUserStatus().firstOrNull()
            ?: return@flow emit(SaveProductStatus.Error.UnregisteredUser)
        if (!user.isEmailVerified) return@flow emit(SaveProductStatus.Error.UnverifiedEmail)
        emit(SaveProductStatus.Loading)
        return@flow emit(
            when (val result = remoteDataSource.saveProduct(productForm.toEntity(user.id))) {
                is SaveProductResult.Error.ConflictingEntityError -> {
                    SaveProductStatus.Error.ProductAlreadyExists(result.id)
                }

                is SaveProductResult.Error.UnexpectedError -> {
                    SaveProductStatus.Error.UnexpectedError(result.error)
                }

                is SaveProductResult.Success -> {
                    SaveProductStatus.Success(result.id)
                }
            }
        )
    }
}
