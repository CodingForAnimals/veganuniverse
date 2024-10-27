package org.codingforanimals.veganuniverse.product.data.config.remote

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.product.data.config.remote.model.ProductConfigDTO
import org.koin.dsl.module

internal class ProductConfigFirebaseSource(
    database: FirebaseDatabase,
) : ProductConfigRemoteSource {

    private val reference = database.getReference(PRODUCT_CONFIG_PATH)

    override suspend fun getProductConfig(): ProductConfigDTO {
        return reference.get().await().getValue(ProductConfigDTO::class.java)!!
    }

    companion object {
        internal const val PRODUCT_CONFIG_PATH = "content/products/config"
    }
}
