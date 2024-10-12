package org.codingforanimals.veganuniverse.additives.domain.repository

import org.codingforanimals.veganuniverse.additives.data.source.local.AdditivesDao
import org.codingforanimals.veganuniverse.additives.data.source.remote.AdditivesRemoteDataSource
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO
import org.codingforanimals.veganuniverse.additives.domain.mapper.toDTO
import org.codingforanimals.veganuniverse.additives.domain.mapper.toDomain
import org.codingforanimals.veganuniverse.additives.domain.mapper.toEntity
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit

internal class AdditiveRepositoryImpl(
    private val remoteDataSource: AdditivesRemoteDataSource,
    private val localDataSource: AdditivesDao,
) : AdditiveRepository {
    override suspend fun getAdditivesFromRemote(): List<Additive> {
        return remoteDataSource.getAdditives().map { it.toDomain() }
    }

    override suspend fun getAdditivesFromLocal(): List<Additive> {
        return localDataSource.getAdditives().map { it.toDomain() }
    }

    override suspend fun queryAdditivesFromLocal(query: String): List<Additive> {
        return localDataSource.queryAdditives(query).map { it.toDomain() }
    }

    override suspend fun getById(id: String): Additive {
        return localDataSource.getById(id).toDomain()
    }

    override suspend fun getByIdFromRemote(id: String): Additive {
        return remoteDataSource.geyById(id).toDomain()
    }

    override suspend fun getEdits(): List<AdditiveEdit> {
        return remoteDataSource.getEdits().map { it.toDomain() }
    }

    override suspend fun getEditByID(editID: String): AdditiveEdit {
        return remoteDataSource.getEditByID(editID).toDomain()
    }

    override suspend fun clearLocalAdditives() {
        return localDataSource.clearAdditives()
    }

    override suspend fun setLocalAdditives(additives: List<Additive>) {
        val entities =
            additives.sortAvoidingCommonDenominator().map { it.toEntity() }.toTypedArray()
        return localDataSource.insertAdditive(*entities)
    }

    override suspend fun sendAdditiveEdit(edit: AdditiveEditDTO) {
        return remoteDataSource.sendAdditiveEdit(edit)
    }

    override suspend fun replaceAdditive(edit: AdditiveEdit) {
        remoteDataSource.replaceAdditive(edit.additive.toDTO())
        remoteDataSource.deleteEdit(edit.id)
    }

    private fun List<Additive>.sortAvoidingCommonDenominator(): List<Additive> {
        fun getCodeWithoutCommonDenominator(code: String): String? = code.split(" ").getOrNull(1)
        return sortedBy { getCodeWithoutCommonDenominator(it.code) }.sortedBy { str ->
            getCodeWithoutCommonDenominator(str.code)?.takeWhile { c -> c.isDigit() }?.toIntOrNull()
                ?: getCodeWithoutCommonDenominator(str.code)?.toIntOrNull()
        }
    }
}
