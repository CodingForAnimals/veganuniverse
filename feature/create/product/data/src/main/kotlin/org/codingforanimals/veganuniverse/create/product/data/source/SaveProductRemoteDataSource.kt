package org.codingforanimals.veganuniverse.create.product.data.source

import org.codingforanimals.veganuniverse.create.product.data.dto.ProductFormDTO
import org.codingforanimals.veganuniverse.create.product.data.model.SaveProductResult

interface SaveProductRemoteDataSource {
    suspend fun saveProduct(product: ProductFormDTO): SaveProductResult
}
