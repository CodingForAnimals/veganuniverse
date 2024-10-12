package org.codingforanimals.veganuniverse.additives.domain.mapper

import org.codingforanimals.veganuniverse.additives.data.config.model.AdditivesConfigDTO
import org.codingforanimals.veganuniverse.additives.data.source.local.AdditiveEntity
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveDTO
import org.codingforanimals.veganuniverse.additives.data.source.remote.model.AdditiveEditDTO
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveEdit
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.domain.model.AdditivesConfig
import org.codingforanimals.veganuniverse.additives.domain.utils.accentInsensitive

internal fun AdditivesConfigDTO.toDomain(): AdditivesConfig {
    return AdditivesConfig(
        version = version ?: 0,
    )
}

internal fun AdditivesConfig.toDTO(): AdditivesConfigDTO {
    return AdditivesConfigDTO(
        version = version,
    )
}

internal fun AdditiveDTO.toDomain(): Additive {
    val id = checkNotNull(objectKey) {
        "AdditiveDTO objectKey cannot be null"
    }
    val code = code
    checkNotNull(code) {
        "AdditiveDTO code cannot be null"
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
    return AdditiveEntity(
        id = id,
        code = code,
        codeAccentInsensitive = code.accentInsensitive(),
        name = name,
        nameAccentInsensitive = name?.accentInsensitive(),
        description = description,
        type = type.name,
    )
}

internal fun Additive.toDTO(): AdditiveDTO {
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
    val code = checkNotNull(code) {
        "AdditiveEditDTO code cannot be null"
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
