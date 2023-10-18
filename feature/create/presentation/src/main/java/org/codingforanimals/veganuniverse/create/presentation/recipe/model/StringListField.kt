package org.codingforanimals.veganuniverse.create.presentation.recipe.model

import org.codingforanimals.veganuniverse.core.ui.viewmodel.ValidationField

internal data class StringListField(
    val list: List<String> = DEFAULT_EMPTY_LIST,
) : ValidationField() {

    override val isValid: Boolean
        get() = list.isNotEmpty() && list.any { it.isNotBlank() }

    fun addItem(): StringListField {
        val ing = list.toMutableList()
        ing.add("")
        return copy(list = ing)
    }

    fun addItemAt(index: Int): StringListField {
        val ing = list.toMutableList()
        ing.add(index, "")
        return copy(list = ing)
    }

    fun deleteItemAt(index: Int): StringListField {
        val ing = list.toMutableList()
        ing.removeAt(index)
        return copy(list = ing)
    }

    fun changeItemAt(index: Int, text: String): StringListField {
        val ing = list.toMutableList()
        ing.removeAt(index)
        ing.add(index, text)
        return copy(list = ing)
    }

    companion object {
        private val DEFAULT_EMPTY_LIST = listOf("", "")
    }
}