package org.codingforanimals.veganuniverse.product.domain.usecase

import android.os.Parcelable
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class UploadProductEdit(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(edit: Product, imageModel: Parcelable?): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to submit a product"
        }

        val originalId = checkNotNull(edit.id) {
            "Product must have an ID to be edited"
        }

        val editWithUserInfo = ProductEdit(
            id = null,
            originalId = originalId,
            editUserId = user.id,
            editUsername = user.name,
            userId = edit.userId,
            username = edit.username,
            name = edit.name,
            brand = edit.brand,
            description = edit.description,
            type = edit.type,
            category = edit.category,
            createdAt = edit.createdAt,
            lastUpdatedAt = edit.lastUpdatedAt,
            imageUrl = edit.imageUrl,
            sourceUrl = edit.sourceUrl,
        )

        productRepository.uploadProductEdit(editWithUserInfo, imageModel)
    }


}
