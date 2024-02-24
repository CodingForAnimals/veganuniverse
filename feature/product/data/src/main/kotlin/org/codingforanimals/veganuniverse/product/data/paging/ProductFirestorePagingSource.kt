package org.codingforanimals.veganuniverse.product.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.product.data.model.Product

class ProductFirestorePagingSource(
    private val queryByName: Query,
) : PagingSource<QuerySnapshot, Product>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Product>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Product> {
        return try {
            val currentPage = params.key ?: queryByName.get().await()
            val lastVisibleItem = currentPage.documents.lastOrNull()
            val nextPage = lastVisibleItem?.let { queryByName.startAfter(it).get().await() }
            LoadResult.Page(
                data = currentPage.toObjects(Product::class.java),
                prevKey = params.key,
                nextKey = nextPage
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "ProductFirestorePagingS"
    }
}
