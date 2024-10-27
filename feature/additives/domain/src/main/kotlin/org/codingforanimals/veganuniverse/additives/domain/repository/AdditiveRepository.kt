package org.codingforanimals.veganuniverse.additives.domain.repository

import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit

interface AdditiveRepository {
    suspend fun uploadAdditive(additive: Additive): String
    suspend fun getAdditivesFromRemote(): List<Additive>
    suspend fun getAdditivesFromLocal(): List<Additive>
    suspend fun queryAdditivesFromLocal(query: String): List<Additive>
    suspend fun getById(id: String): Additive
    suspend fun getByIdFromRemote(id: String): Additive
    suspend fun getEdits(): List<AdditiveEdit>
    suspend fun getEditByID(editID: String): AdditiveEdit
    suspend fun clearLocalAdditives()
    suspend fun setLocalAdditives(additives: List<Additive>)
    suspend fun sendAdditiveEdit(edit: AdditiveEditDTO)
    suspend fun replaceAdditive(edit: AdditiveEdit)
}
