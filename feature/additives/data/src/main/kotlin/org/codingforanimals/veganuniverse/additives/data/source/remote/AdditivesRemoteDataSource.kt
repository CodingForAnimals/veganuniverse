package org.codingforanimals.veganuniverse.additives.data.source.remote

import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveDTO
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO

interface AdditivesRemoteDataSource {
    suspend fun getAdditives(): List<AdditiveDTO>
    suspend fun geyById(id: String): AdditiveDTO
    suspend fun getEdits(): List<AdditiveEditDTO>
    suspend fun getEditByID(editID: String): AdditiveEditDTO
    suspend fun sendAdditiveEdit(edit: AdditiveEditDTO)
    suspend fun replaceAdditive(additiveDTO: AdditiveDTO)
    suspend fun deleteEdit(editID: String)
}

