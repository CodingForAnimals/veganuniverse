package org.codingforanimals.veganuniverse.product.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.product.data.model.ProductFirestoreEntity

private const val TAG = "ProductPagingSource"

internal class ProductPagingSource(private val query: Query) :
    PagingSource<QuerySnapshot, ProductFirestoreEntity>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, ProductFirestoreEntity>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, ProductFirestoreEntity> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleItem = currentPage.documents.lastOrNull()
            val nextPage = lastVisibleItem?.let { query.startAfter(it).get().await() }
            LoadResult.Page(
                data = currentPage.toObjects(ProductFirestoreEntity::class.java),
                prevKey = params.key,
                nextKey = nextPage,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LoadResult.Error(e)
        }
    }
}