package org.codingforanimals.veganuniverse.recipes.presentation.create.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.ValidationField
import org.codingforanimals.veganuniverse.recipes.presentation.model.toUI

internal data class TagsField(
    val tags: List<Tag> = RecipeTag.entries.toTypedArray().toViewEntity(),
) : ValidationField() {
    override val isValid: Boolean
        get() = tags.any { it.selected }

    fun toggleTag(name: Int): TagsField {
        val tagIndex = tags.indexOfFirst { it.label == name }
        if (tagIndex == -1) return this

        val updatedTag = tags.getOrNull(tagIndex)?.let {
            it.copy(selected = !it.selected)
        } ?: return this

        val mutableTags = tags.toMutableList()
        mutableTags.removeAt(tagIndex)
        mutableTags.add(tagIndex, updatedTag)

        return copy(tags = mutableTags)
    }
}

internal data class Tag(
    val name: String,
    @StringRes val label: Int,
    val icon: Icon,
    val selected: Boolean = false,
)

private fun Array<RecipeTag>.toViewEntity(): List<Tag> {
    return map {
        val tagUI = it.toUI()
        Tag(
            name = it.name,
            label = tagUI.label,
            icon = tagUI.icon,
        )
    }
}