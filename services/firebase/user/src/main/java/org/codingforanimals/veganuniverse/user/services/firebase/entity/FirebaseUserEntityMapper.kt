package org.codingforanimals.veganuniverse.user.services.firebase.entity

import com.google.firebase.auth.FirebaseUser
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.StorageBucketWrapper
import org.codingforanimals.veganuniverse.services.firebase.StoragePath

internal class FirebaseUserEntityMapper(
    storageBucketWrapper: StorageBucketWrapper,
) : OneWayEntityMapper<FirebaseUser, UserFirebaseEntity> {

    private val storageBucketPath = storageBucketWrapper.storageBucket

    override fun map(obj: FirebaseUser): UserFirebaseEntity {
        return with(obj) {
            UserFirebaseEntity(
                userId = uid,
                name = displayName ?: email ?: "",
                email = email ?: "",
                isEmailVerified = isEmailVerified,
                profilePictureUrl = StoragePath.getUserPictureRef(storageBucketPath, uid)
            )
        }
    }

}