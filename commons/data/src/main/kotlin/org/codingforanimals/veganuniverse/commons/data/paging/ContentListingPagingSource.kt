package org.codingforanimals.veganuniverse.commons.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ContentListingPagingSource<T: Any>(
    private val ids: List<String>,
    private val pageSize: Int,
    private val query: suspend (List<String>) -> (List<T>),
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        if (ids.isEmpty()) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null,
            )
        }
        val page = params.key ?: 1
        val indexFrom = pageSize * (page - 1)
        val indexTo = (page * pageSize)
        val safeIndexTo = minOf(indexTo, ids.size)
        val currentPage = ids.subList(indexFrom, safeIndexTo)
        val data = query(currentPage)
        return LoadResult.Page(
            data = data,
            prevKey = params.key,
            nextKey = (page + 1).takeIf { safeIndexTo < ids.size },
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}
