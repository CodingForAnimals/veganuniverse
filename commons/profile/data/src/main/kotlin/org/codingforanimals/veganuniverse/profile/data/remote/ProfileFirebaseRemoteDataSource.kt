package org.codingforanimals.veganuniverse.profile.data.remote

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditActionValue
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditArguments
import org.codingforanimals.veganuniverse.profile.data.model.ProfileEditContentType

internal class ProfileFirebaseRemoteDataSource(
    private val profilesReference: DatabaseReference,
) : ProfileRemoteDataSource {
    override suspend fun getProfile(userId: String): ProfileDTO? {
        return profilesReference.child(userId).get().await().getValue(ProfileDTO::class.java)
    }

    override suspend fun editProfile(args: ProfileEditArguments) {
        with(args) {
            profilesReference
                .child(userId)
                .child(profileEditActionType.getFirebasePath())
                .child(profileEditContentType.getFirebasePath())
                .child(contentId)
                .setValue(profileEditActionValue.getFirebaseValue())
                .await()
        }
    }

    private fun ProfileEditActionType.getFirebasePath(): String {
        return when (this) {
            ProfileEditActionType.BOOKMARK -> BOOKMARKS_PATH
            ProfileEditActionType.LIKE -> LIKES_PATH
            ProfileEditActionType.CONTRIBUTION -> CONTRIBUTIONS_PATH
        }
    }

    private fun ProfileEditContentType.getFirebasePath(): String {
        return when (this) {
            ProfileEditContentType.PLACE -> PLACES_PATH
            ProfileEditContentType.PRODUCT -> PRODUCTS_PATH
            ProfileEditContentType.RECIPE -> RECIPES_PATH
        }
    }

    private fun ProfileEditActionValue.getFirebaseValue(): Boolean? {
        return when (this) {
            ProfileEditActionValue.ADD -> true
            ProfileEditActionValue.REMOVE -> null
        }
    }

    companion object {
        private const val LIKES_PATH = "likes"
        private const val BOOKMARKS_PATH = "bookmarks"
        private const val CONTRIBUTIONS_PATH = "contributions"
        private const val PLACES_PATH = "places"
        private const val RECIPES_PATH = "recipes"
        private const val PRODUCTS_PATH = "products"
    }
}
