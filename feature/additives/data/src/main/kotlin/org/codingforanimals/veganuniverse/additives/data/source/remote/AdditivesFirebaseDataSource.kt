package org.codingforanimals.veganuniverse.additives.data.source.remote

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveDTO
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO
import org.codingforanimals.veganuniverse.commons.analytics.Analytics

internal class AdditivesFirebaseDataSource(
    private val additivesReference: DatabaseReference,
    private val editsReference: DatabaseReference,
) : AdditivesRemoteDataSource {
    override suspend fun getAdditives(): List<AdditiveDTO> {
        return additivesReference.get().await()
            .children.mapNotNull { additiveSnapshot ->
                val additiveDTO = additiveSnapshot
                    .getValue(AdditiveDTO::class.java)
                    ?.copy(objectKey = additiveSnapshot.key!!)
                if (additiveDTO == null) {
                    Analytics.logNonFatalException(Exception("AdditiveDTO is null at key ${additiveSnapshot.key}"))
                }
                additiveDTO
            }
    }

    override suspend fun geyById(id: String): AdditiveDTO {
        return additivesReference
            .child(id)
            .get().await()
            .let {
                it.getValue(AdditiveDTO::class.java)!!.copy(objectKey = it.key!!)
            }
    }

    override suspend fun getEdits(): List<AdditiveEditDTO> {
        return editsReference.get().await()
            .children
            .mapNotNull { additiveEditSnapshot ->
                val editDTO = additiveEditSnapshot
                    .getValue(AdditiveEditDTO::class.java)
                    ?.copy(objectKey = additiveEditSnapshot.key!!)
                if (editDTO == null) {
                    Analytics.logNonFatalException(Exception("AdditiveEditDTO is null at key ${additiveEditSnapshot.key}"))
                }
                editDTO
            }
    }

    override suspend fun getEditByID(editID: String): AdditiveEditDTO {
        return editsReference
            .child(editID)
            .get().await().let {
                it.getValue(AdditiveEditDTO::class.java)!!.copy(objectKey = it.key!!)
            }
    }

    override suspend fun sendAdditiveEdit(edit: AdditiveEditDTO) {
        checkNotNull(edit.additiveID) {
            "AdditiveEditDTO additiveID is required"
        }
        checkNotNull(edit.userID) {
            "AdditiveEditDTO userID is required"
        }
        editsReference
            .push()
            .setValue(edit)
            .await()
    }

    override suspend fun replaceAdditive(additiveDTO: AdditiveDTO) {
        val objectKey = checkNotNull(additiveDTO.objectKey) {
            "AdditiveDTO objectKey is required"
        }
        check(!additiveDTO.code.isNullOrEmpty()) {
            "AdditiveDTO code is required"
        }
        additivesReference
            .child(objectKey)
            .setValue(additiveDTO)
    }

    override suspend fun deleteEdit(editID: String) {
        editsReference
            .child(editID)
            .removeValue()
            .await()
    }

    companion object {
        internal const val ADDITIVES_PATH = "content/additives/items"
        internal const val EDITS_PATH = "content/additives/edits"
    }
}