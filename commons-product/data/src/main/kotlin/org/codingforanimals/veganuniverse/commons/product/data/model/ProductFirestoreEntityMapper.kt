package org.codingforanimals.veganuniverse.commons.product.data.model

import org.codingforanimals.veganuniverse.commons.product.shared.model.Product

interface ProductFirestoreEntityMapper {
    fun mapToModel(firestoreEntity: ProductFirestoreEntity): Product
}

