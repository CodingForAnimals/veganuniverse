package org.codingforanimals.veganuniverse.additives.domain.model

enum class AdditiveType {
    VEGAN,
    NOT_VEGAN,
    DOUBTFUL,
    UNKNOWN,
    ;

    companion object {
        fun fromString(value: String?): AdditiveType {
            return AdditiveType.entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}
