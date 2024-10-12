package org.codingforanimals.veganuniverse.additives.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AdditiveEntity(
    @PrimaryKey val id: String,
    val code: String,
    val codeAccentInsensitive: String,
    val name: String?,
    val nameAccentInsensitive: String?,
    val description: String?,
    val type: String,
)