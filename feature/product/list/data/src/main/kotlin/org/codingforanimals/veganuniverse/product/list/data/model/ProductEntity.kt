package org.codingforanimals.veganuniverse.product.list.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class ProductEntity(
    @DocumentId val id: String? = null,
    val name: String = "",
    val brand: String = "",
    val comment: String? = null,
    val type: String = "",
    val category: String = "",
    val userId: String = "",
    val username: String = "",
    val createdAt: Timestamp? = null,
    val imageStorageRef: String? = null,
) {
    val date: Date?
        get() = createdAt?.toDate()
}
