package org.codingforanimals.veganuniverse.commons.recipe.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.recipe.data.remote.entity.RecipeFirestoreEntity

private const val TAG = "RecipePagingSource"

internal class RecipePagingSource(
    private val query: Query,
) : PagingSource<QuerySnapshot, RecipeFirestoreEntity>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, RecipeFirestoreEntity>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, RecipeFirestoreEntity> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleItem = currentPage.documents.lastOrNull()
            val nextPage = lastVisibleItem?.let { query.startAfter(it).get().await() }
            LoadResult.Page(
                data = currentPage.toObjects(RecipeFirestoreEntity::class.java),
                prevKey = params.key,
                nextKey = nextPage,
            )
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LoadResult.Error(e)
        }
    }

}
