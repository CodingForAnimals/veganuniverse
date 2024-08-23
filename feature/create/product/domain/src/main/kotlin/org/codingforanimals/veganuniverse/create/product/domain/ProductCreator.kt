package org.codingforanimals.veganuniverse.create.product.domain

import org.codingforanimals.veganuniverse.product.entity.Product

interface ProductCreator {
    suspend fun submitProduct(product: Product)
}

