package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class UploadProductsBatch(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(products: String) {
        val productList = products.split(":item:").map { productRaw ->
            val fields = productRaw.split(":field:")

            val name = fields[1]
            check(name.isNotBlank()) {
                "Product name cannot be blank"
            }

            val brand = fields[0]
            check(brand.isNotBlank()) {
                "Product brand cannot be blank"
            }

            val categoryRaw = fields[3]
            check(categoryRaw.isNotBlank()) {
                "Product category cannot be null"
            }

            val typeRaw = fields[5]
            check(typeRaw.isNotBlank()) {
                "Product type cannot be null"
            }

            Product(
                id = null,
                userId = null,
                username = "Coding for Animals",
                name = name,
                brand = brand,
                description = fields[2],
                category = ProductCategory.fromString(categoryRaw)!!,
                sourceUrl = fields[4],
                type = ProductType.fromString(typeRaw)!!,
                createdAt = null,
                lastUpdatedAt = null,
                imageUrl = null,
            )
        }
        productRepository.uploadValidatedProductsBatch(productList)
    }
}
