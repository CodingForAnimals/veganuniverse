package org.codingforanimals.veganuniverse.additives.domain.model

data class Additive(
    val id: String,
    val code: String,
    val name: String?,
    val description: String?,
    val type: AdditiveType,
) {
    companion object {
        fun mock(
            id: String = "123",
            code: String = "INS 100",
            name: String? = "Curcumina",
            description: String? = "La curcumina es apto vegan",
            type: AdditiveType = AdditiveType.VEGAN,
        ) = Additive(
            id = id,
            code = code,
            name = name,
            description = description,
            type = type,
        )
    }
}
