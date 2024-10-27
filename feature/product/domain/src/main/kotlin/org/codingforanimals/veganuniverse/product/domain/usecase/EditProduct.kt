package org.codingforanimals.veganuniverse.product.domain.usecase

import android.os.Parcelable
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class EditProduct(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productEdit: ProductEdit, imageModel: Parcelable?): Result<Unit> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().first()) {
                "User must be logged in to edit a product"
            }
            val edit = productEdit.copy(
                editUserId = user.id,
                editUsername = user.name,
            )
            productRepository.uploadProductEdit(edit, imageModel)
        }.onFailure {
            Analytics.logNonFatalException(it)
        }
}
