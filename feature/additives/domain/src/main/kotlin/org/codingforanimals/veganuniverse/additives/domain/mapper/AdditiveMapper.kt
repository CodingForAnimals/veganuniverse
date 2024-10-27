package org.codingforanimals.veganuniverse.additives.domain.mapper

import org.codingforanimals.veganuniverse.additives.data.config.local.model.AdditivesConfigLocalModel
import org.codingforanimals.veganuniverse.additives.data.config.remote.model.AdditivesConfigDTO
import org.codingforanimals.veganuniverse.additives.data.source.local.model.AdditiveEntity
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveDTO
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.domain.model.AdditivesConfig
import org.codingforanimals.veganuniverse.commons.data.utils.accentInsensitive

internal fun AdditivesConfigLocalModel.toDomain(): AdditivesConfig {
    return AdditivesConfig(
        version = version,
    )
}

internal fun AdditivesConfigDTO.toDomain(): AdditivesConfig {
    val version = checkNotNull(version) {
        "AdditivesConfigDTO version cannot be null"
    }
    return AdditivesConfig(
        version = version,
    )
}

internal fun AdditivesConfig.toDto(): AdditivesConfigLocalModel {
    return AdditivesConfigLocalModel(
        version = version,
    )
}

internal fun AdditiveDTO.toDomain(): Additive {
    val id = checkNotNull(objectKey) {
        "AdditiveDTO objectKey cannot be null"
    }
    return Additive(
        id = id,
        code = code,
        name = name,
        description = description,
        type = AdditiveType.fromString(type),
    )
}

internal fun AdditiveEntity.toDomain(): Additive {
    return Additive(
        id = id,
        code = code,
        name = name,
        description = description,
        type = AdditiveType.fromString(type),
    )
}

internal fun Additive.toEntity(): AdditiveEntity {
    val id = checkNotNull(id) {
        "Additive id cannot be null"
    }
    return AdditiveEntity(
        id = id,
        code = code,
        codeAccentInsensitive = code?.accentInsensitive(),
        name = name,
        nameAccentInsensitive = name?.accentInsensitive(),
        description = description,
        type = type.name,
    )
}

internal fun Additive.toDto(): AdditiveDTO {
    return AdditiveDTO(
        objectKey = id,
        code = code,
        name = name,
        description = description,
        type = type.name,
    )
}

internal fun Additive.toEditDTO(userID: String): AdditiveEditDTO {
    return AdditiveEditDTO(
        additiveID = id,
        userID = userID,
        code = code,
        name = name,
        description = description,
        type = type.name,
    )
}

internal fun AdditiveEditDTO.toDomain(): AdditiveEdit {
    val objectKey = checkNotNull(objectKey) {
        "AdditiveEditDTO objectKey cannot be null"
    }
    val additiveID = checkNotNull(additiveID) {
        "AdditiveEditDTO additiveID cannot be null"
    }
    val userID = checkNotNull(userID) {
        "AdditiveEditDTO userID cannot be null"
    }
    return AdditiveEdit(
        id = objectKey,
        additiveID = additiveID,
        userID = userID,
        code = code,
        name = name,
        description = description,
        type = AdditiveType.fromString(type),
    )
}
