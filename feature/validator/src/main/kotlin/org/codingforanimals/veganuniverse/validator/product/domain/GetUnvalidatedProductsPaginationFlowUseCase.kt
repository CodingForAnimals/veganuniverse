package org.codingforanimals.veganuniverse.validator.product.domain

import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter

internal class GetUnvalidatedProductsPaginationFlowUseCase(
    private val productRepository: ProductRepository,
) {
    operator fun invoke() = productRepository.queryProductsPagingDataFlow(
        ProductQueryParams.Builder().validated(false).withPageSize(2).withSorter(ProductSorter.DATE)
            .build()
    )
}
