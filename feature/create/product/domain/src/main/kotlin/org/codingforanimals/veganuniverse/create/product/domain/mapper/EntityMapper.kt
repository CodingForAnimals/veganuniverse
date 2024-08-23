package org.codingforanimals.veganuniverse.create.product.domain.mapper

import org.codingforanimals.veganuniverse.create.product.data.dto.ProductFormDTO
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm

private fun createKeywords(vararg searchableFields: String): List<String> {
    val keywords = mutableListOf<String>()
    fun addToKeywordsIfLongEnough(string: String, index: Int) {
        if (index >= 2) {
            keywords.add(string.substring(0, index + 1).lowercase())
        }
    }
    searchableFields.forEach { string ->

        string.forEachIndexed { index, _ ->
            addToKeywordsIfLongEnough(string, index)
        }
        string.split(" ").takeIf { it.size > 1 }?.forEachIndexed { index, word ->
            if (index >= 1) {
                word.forEachIndexed { wordIndex, _ ->
                    addToKeywordsIfLongEnough(word, wordIndex)
                }
            }
        }
    }
    return keywords
}

fun ProductForm.toEntity(userId: String): ProductFormDTO {
    return ProductFormDTO(
        userId = userId,
        name = name,
        brand = brand,
        category = category,
        type = type,
        comment = comment,
        imageModel = imageModel,
        keywords = createKeywords(name, brand)
    )
}