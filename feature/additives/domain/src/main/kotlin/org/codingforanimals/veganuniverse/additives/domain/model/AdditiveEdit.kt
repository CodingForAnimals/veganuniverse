package org.codingforanimals.veganuniverse.additives.domain.model

data class AdditiveEdit(
    val id: String,
    val additiveID: String,
    val userID: String,
    val code: String,
    val name: String?,
    val description: String?,
    val type: AdditiveType,
) {
    val additive: Additive
        get() = Additive(additiveID, code, name, description, type)

    companion object {
        fun mock(
            id: String = "123",
            additiveID: String = "123123",
            userID: String = "1234",
            code: String = "INS 100",
            name: String? = "Curcumina",
            description: String? = "La curcumina es apto vegan",
            type: AdditiveType = AdditiveType.VEGAN,
        ) = AdditiveEdit(
            id = id,
            additiveID = additiveID,
            userID = userID,
            code = code,
            name = name,
            description = description,
            type = type,
        )
    }
}
