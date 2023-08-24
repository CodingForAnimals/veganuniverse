package org.codingforanimals.veganuniverse.services.firebase.api

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.common.utils.AverageCalculator
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.services.firebase.DatabaseFields
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.FirestoreFields
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceReview
import org.codingforanimals.veganuniverse.services.firebase.model.PaginationCursor
import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

private const val TAG = "PlaceReviewsFirebaseApi"

internal class PlaceReviewsFirebaseApi(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val placeReviewToDomainEntityMapper: OneWayEntityMapper<PlaceReview, PlaceReviewDomainEntity>,
    private val placeReviewFormToFirebaseEntityMapper: OneWayEntityMapper<PlaceReviewForm, PlaceReview>,
) : PlaceReviewsApi {

    override suspend fun fetchReview(
        placeId: String,
        userId: String,
    ): PlaceReviewDomainEntity? {
        val snap = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .whereEqualTo(FirestoreFields.USER_ID, userId)
            .get().await()
        return snap
            .documents
            .firstOrNull()
            ?.toObject(PlaceReview::class.java)
            ?.let { placeReviewToDomainEntityMapper.map(it) }
    }

    private var cursor: PaginationCursor = PaginationCursor.FreshStart
    override suspend fun fetchReviews(placeId: String): PaginatedResponse<PlaceReviewDomainEntity> {
        var query = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .orderBy(FirestoreFields.Places.TIMESTAMP, Query.Direction.DESCENDING)

        when (val currentCursor = cursor) {
            PaginationCursor.FreshStart -> Unit
            PaginationCursor.Finished -> return PaginatedResponse(emptyList(), false)
            is PaginationCursor.Current -> {
                query = query.startAt(currentCursor.cursor)
            }
        }

        val snap = query.limit(3).get().await()

        val reviews = snap.documents.toMutableList()
        val (hasMoreItems, updatedCursor) = if (reviews.size == 3) {
            Pair(true, PaginationCursor.Current(reviews.removeLast()))
        } else {
            Pair(false, PaginationCursor.Finished)
        }

        cursor = updatedCursor

        return PaginatedResponse(
            content = reviews.mapNotNull { reviewSnapshot ->
                reviewSnapshot.toObject(PlaceReview::class.java)?.let { placeReview ->
                    placeReviewToDomainEntityMapper.map(placeReview)
                }
            },
            hasMoreItems = hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReviewDomainEntity {
        val placeReview = placeReviewFormToFirebaseEntityMapper.map(placeReviewForm)
        val docRef = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .add(placeReview).await()

//        manipulatePlaceRating(placeId, placeReviewForm.rating, AverageCalculator.Addition)

        return with(placeReviewForm) {
            PlaceReviewDomainEntity(
                id = docRef.id,
                userId = userId,
                username = username,
                rating = rating,
                title = title,
                description = description,
                timestampSeconds = Timestamp.now().seconds,
            )
        }
    }

    private fun manipulatePlaceRating(
        placeId: String,
        rating: Int,
        averageCalculator: AverageCalculator,
    ) {
        firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .count()
            .get(AggregateSource.SERVER).addOnSuccessListener { aggregateQuery ->
                database
                    .getReference(DatabasePath.Content.Places.card(placeId))
                    .child(DatabaseFields.Content.Places.Cards.RATING)
                    .runTransaction(
                        object : Transaction.Handler {
                            override fun doTransaction(currentData: MutableData): Transaction.Result {
                                val currentRatingAverage = currentData.getValue<Double>()
                                if (currentRatingAverage != null) {
                                    currentData.value = averageCalculator.calculate(
                                        value = rating,
                                        average = currentRatingAverage,
                                        count = aggregateQuery.count
                                    )
                                }
                                return Transaction.success(currentData)
                            }

                            override fun onComplete(
                                error: DatabaseError?,
                                committed: Boolean,
                                currentData: DataSnapshot?,
                            ) {
                                Log.e(TAG, "onComplete: $error")
                            }
                        },
                    )
            }
    }

    override suspend fun deleteReview(placeId: String, placeReview: PlaceReviewDomainEntity) {
        firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .document(placeReview.id).delete().await()
        manipulatePlaceRating(placeId, placeReview.rating, AverageCalculator.Subtraction)
    }
}