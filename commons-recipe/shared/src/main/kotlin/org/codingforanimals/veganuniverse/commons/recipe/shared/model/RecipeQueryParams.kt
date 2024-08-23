package org.codingforanimals.veganuniverse.commons.recipe.shared.model

data class RecipeQueryParams internal constructor(
    val title: String?,
    val tag: RecipeTag?,
    val sorter: RecipeSorter,
    val pageSize: Int,
    val maxSize: Int,
) {
    class Builder {
        private var title: String? = null
        private var tag: RecipeTag? = null
        private var sorter: RecipeSorter = RecipeSorter.DATE
        private var pageSize: Int = 10
        private var maxSize: Int = Int.MAX_VALUE

        fun withTitle(value: String?): Builder {
            title = value
            return this
        }

        fun withTag(value: RecipeTag?): Builder {
            tag = value
            return this
        }

        fun withSorter(value: RecipeSorter?): Builder {
            value?.let { sorter = it }
            return this
        }

        fun withPageSize(value: Int?): Builder {
            value?.let { pageSize = it }
            return this
        }

        fun withMaxSize(value: Int?): Builder {
            value?.let { maxSize = it }
            return this
        }

        fun build() = RecipeQueryParams(
            title = title,
            tag = tag,
            sorter = sorter,
            pageSize = pageSize,
            maxSize = maxSize,
        )
    }
}