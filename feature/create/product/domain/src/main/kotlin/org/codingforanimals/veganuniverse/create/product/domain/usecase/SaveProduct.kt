package org.codingforanimals.veganuniverse.create.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm

interface SaveProduct {
    suspend operator fun invoke(productForm: ProductForm): Flow<SaveProductStatus>
}
