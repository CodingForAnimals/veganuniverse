package org.codingforanimals.veganuniverse.commons.product.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class ProductFirestoreEntity(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val nameLowercase: String? = null,
    val brand: String? = null,
    val brandLowercase: String? = null,
    val keywords: List<String>? = null,
    val comment: String? = null,
    val type: String? = null,
    val category: String? = null,
    val imageId: String? = null,
    val validated: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
)
