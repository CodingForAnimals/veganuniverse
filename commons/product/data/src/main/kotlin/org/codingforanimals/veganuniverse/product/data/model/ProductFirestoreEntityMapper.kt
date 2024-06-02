package org.codingforanimals.veganuniverse.product.data.model

import org.codingforanimals.veganuniverse.product.model.Product

interface ProductFirestoreEntityMapper {
    fun mapToModel(firestoreEntity: ProductFirestoreEntity): Product
}

