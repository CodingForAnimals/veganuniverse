package org.codingforanimals.veganuniverse.additives.data.config.remote

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.additives.data.config.model.AdditivesConfigDTO

internal class AdditivesConfigFirebaseSource(
    private val additivesConfigReference: DatabaseReference,
) : AdditivesConfigRemoteSource {
    override suspend fun getAdditivesConfig(): AdditivesConfigDTO? {
        return additivesConfigReference.get().await().getValue(AdditivesConfigDTO::class.java)
    }

    companion object {
        internal const val ADDITIVES_CONFIG_PATH = "content/additives/config"
    }
}