package org.codingforanimals.veganuniverse.create.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.create.product.domain.model._ProductForm

interface SaveProduct {
    suspend operator fun invoke(productForm: _ProductForm): Flow<SaveProductStatus>
}
