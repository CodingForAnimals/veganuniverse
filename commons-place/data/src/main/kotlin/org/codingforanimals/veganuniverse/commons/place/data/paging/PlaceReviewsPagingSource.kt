package org.codingforanimals.veganuniverse.commons.place.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.place.data.model.PlaceReviewFirestoreEntity

internal class PlaceReviewsPagingSource(private val query: Query) :
    PagingSource<QuerySnapshot, PlaceReviewFirestoreEntity>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, PlaceReviewFirestoreEntity>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PlaceReviewFirestoreEntity> {
        val currentPage = params.key ?: query.get().await()
        val lastVisibleItem = currentPage.documents.lastOrNull()
        val nextPage = lastVisibleItem?.let { query.startAfter(it).get().await() }
        return LoadResult.Page(
            data = currentPage.toObjects(PlaceReviewFirestoreEntity::class.java),
            prevKey = params.key,
            nextKey = nextPage,
        )
    }
}