package org.codingforanimals.veganuniverse.create.product.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

internal data class ProductFormFirestoreDTO(
    val userId: String,
    val name: String,
    val brand: String,
    val category: String,
    val type: String,
    val comment: String?,
    val imageStorageRef: String,
    val keywords: List<String>,
    @ServerTimestamp val createdAt: Timestamp? = null,
)
