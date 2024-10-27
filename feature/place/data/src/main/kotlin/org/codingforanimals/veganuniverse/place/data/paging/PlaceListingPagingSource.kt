package org.codingforanimals.veganuniverse.place.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.codingforanimals.veganuniverse.place.data.model.PlaceFirestoreEntity

internal class PlaceListingPagingSource(
    private val ids: List<String>,
    private val pageSize: Int,
    private val query: suspend (List<String>) -> List<PlaceFirestoreEntity>,
) : PagingSource<Int, PlaceFirestoreEntity>() {
    override fun getRefreshKey(state: PagingState<Int, PlaceFirestoreEntity>): Int? =
        null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceFirestoreEntity> {
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
}