package org.codingforanimals.veganuniverse.place.data.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.codingforanimals.veganuniverse.place.data.mapper.PlaceEntityMapper
import org.codingforanimals.veganuniverse.place.data.mapper.PlaceEntityMapperImpl
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource.Companion.PLACES_CARDS
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource.Companion.PLACES_GEOFIRE
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource.Companion.PLACES_ITEMS_COLLECTION
import org.codingforanimals.veganuniverse.place.data.source.PlaceFirebaseDataSource.Companion.PLACES_REPORTS
import org.codingforanimals.veganuniverse.place.data.source.PlaceRemoteDataSource
import org.codingforanimals.veganuniverse.place.data.source.PlaceReviewFirestoreDataSource
import org.codingforanimals.veganuniverse.place.data.source.PlaceReviewFirestoreDataSource.Companion.PLACES_REVIEW_REPORTS
import org.codingforanimals.veganuniverse.place.data.source.PlaceReviewRemoteDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val placeDataModule = module {
    factoryOf(::PlaceEntityMapperImpl) bind PlaceEntityMapper::class
    factory<PlaceRemoteDataSource> {
        PlaceFirebaseDataSource(
            geoFireReference = FirebaseDatabase.getInstance().getReference(PLACES_GEOFIRE),
            cardsReference = FirebaseDatabase.getInstance().getReference(PLACES_CARDS),
            placesCollection = FirebaseFirestore.getInstance().collection(PLACES_ITEMS_COLLECTION),
            placesReportsReference = FirebaseDatabase.getInstance().getReference(PLACES_REPORTS),
            uploadPictureUseCase = get(),
            mapper = get(),
        )
    }

    factory<PlaceReviewRemoteDataSource> {
        PlaceReviewFirestoreDataSource(
            placesCollection = FirebaseFirestore.getInstance().collection(PLACES_ITEMS_COLLECTION),
            placeReviewReportsReference = FirebaseDatabase.getInstance()
                .getReference(PLACES_REVIEW_REPORTS),
        )
    }
}