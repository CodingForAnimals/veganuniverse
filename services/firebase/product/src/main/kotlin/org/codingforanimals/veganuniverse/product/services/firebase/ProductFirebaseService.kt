package org.codingforanimals.veganuniverse.product.services.firebase

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.product.entity.Product
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath

internal class ProductFirebaseService(
    private val database: FirebaseDatabase,
) : ProductService {
    override suspend fun create(product: Product): String {
        val ref = database
            .getReference(DatabasePath.Product.ITEMS)
            .push()
        ref.setValue(product).await()
        return ref.key!!
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

}