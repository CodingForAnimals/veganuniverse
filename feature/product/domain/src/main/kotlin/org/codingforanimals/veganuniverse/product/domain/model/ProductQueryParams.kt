package org.codingforanimals.veganuniverse.product.domain.model

data class ProductQueryParams internal constructor(
    val keyword: String?,
    val name: String?,
    val brand: String?,
    val type: ProductType?,
    val category: ProductCategory?,
    val sorter: ProductSorter,
    val pageSize: Int,
    val maxSize: Int,
    val validated: Boolean?,
) {
    class Builder {
        private var keyword: String? = null
        private var name: String? = null
        private var brand: String? = null
        private var type: ProductType? = null
        private var category: ProductCategory? = null
        private var sorter: ProductSorter = ProductSorter.NAME
        private var pageSize: Int = 10
        private var maxSize: Int = Int.MAX_VALUE
        private var validated: Boolean? = true

        fun withKeyword(value: String?): Builder {
            keyword = value
            return this
        }

        fun withExactSearch(name: String, brand: String?): Builder {
            this.name = name
            this.brand = brand
            return this
        }

        fun withType(value: ProductType?): Builder {
            type = value
            return this
        }

        fun withCategory(value: ProductCategory?): Builder {
            category = value
            return this
        }

        fun withSorter(value: ProductSorter?): Builder {
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

        fun validated(value: Boolean?): Builder {
            value?.let { validated = it }
            return this
        }

        fun build() = ProductQueryParams(
            keyword = keyword,
            name = name,
            brand = brand,
            type = type,
            category = category,
            sorter = sorter,
            pageSize = pageSize,
            maxSize = maxSize,
            validated = validated,
        )
    }
}