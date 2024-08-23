package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter

class GetLatestProducts(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): List<Product> {
        val params = ProductQueryParams.Builder()
            .withSorter(ProductSorter.DATE)
            .withMaxSize(3)
            .withPageSize(3)
            .build()
        return productRepository.queryProducts(params)
    }
}
