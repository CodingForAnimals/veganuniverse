package org.codingforanimals.veganuniverse.place.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.place.data.model.PlaceFirestoreEntity

private const val TAG = "ProductPagingSource"

internal class PlacePagingSource(private val query: Query) :
    PagingSource<QuerySnapshot, PlaceFirestoreEntity>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, PlaceFirestoreEntity>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PlaceFirestoreEntity> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleItem = currentPage.documents.lastOrNull()
            val nextPage = lastVisibleItem?.let { query.startAfter(it).get().await() }
            LoadResult.Page(
                data = currentPage.toObjects(PlaceFirestoreEntity::class.java),
                prevKey = params.key,
                nextKey = nextPage,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LoadResult.Error(e)
        }
    }
}