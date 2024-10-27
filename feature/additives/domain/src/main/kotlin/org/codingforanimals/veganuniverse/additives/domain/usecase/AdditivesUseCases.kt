package org.codingforanimals.veganuniverse.additives.domain.usecase

import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.additives.domain.mapper.toEditDTO
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditiveRepository
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.data.utils.accentInsensitive
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class AdditivesUseCases(
    private val additiveRepository: AdditiveRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend fun uploadAdditive(additive: Additive): Result<String> = runCatching {
        additiveRepository.uploadAdditive(additive)
    }.onFailure { throwable ->
        Analytics.logNonFatalException(throwable)
    }

    suspend fun getAllAdditives(): List<Additive> {
        return try {
            additiveRepository.getAdditivesFromLocal()
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun queryAdditives(query: String): List<Additive> {
        return try {
            additiveRepository.queryAdditivesFromLocal(query.accentInsensitive())
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun getByIdFromLocal(id: String): Additive {
        return try {
            additiveRepository.getById(id)
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun getByIdFromRemote(id: String): Additive {
        return try {
            additiveRepository.getByIdFromRemote(id)
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun getEdits(): List<AdditiveEdit> {
        return try {
            additiveRepository.getEdits()
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun getEditByID(editID: String): AdditiveEdit {
        return try {
            additiveRepository.getEditByID(editID)
        } catch (throwable: Throwable) {
            Analytics.logNonFatalException(throwable)
            throw throwable
        }
    }

    suspend fun sendEdit(additive: Additive): Result<Unit> = runCatching {
        val userID = flowOnCurrentUser().first()!!.id
        additiveRepository.sendAdditiveEdit(additive.toEditDTO(userID))
    }.onFailure { throwable ->
        Analytics.logNonFatalException(throwable)
        throw throwable
    }

    suspend fun validateEdit(editID: String): Result<Unit> = runCatching {
        val edit = getEditByID(editID)
        additiveRepository.replaceAdditive(edit)
    }.onFailure { throwable ->
        Analytics.logNonFatalException(throwable)
        throw throwable
    }
}
