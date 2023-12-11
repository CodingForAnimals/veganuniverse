package org.codingforanimals.veganuniverse.product.services.firebase

import org.codingforanimals.veganuniverse.product.entity.Product

interface ProductService {
    suspend fun create(product: Product): String
    suspend fun delete(id: String)
}

