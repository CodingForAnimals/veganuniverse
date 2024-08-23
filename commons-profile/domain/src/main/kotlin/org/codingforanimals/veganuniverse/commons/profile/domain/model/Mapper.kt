package org.codingforanimals.veganuniverse.commons.profile.domain.model

import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditActionType
import org.codingforanimals.veganuniverse.commons.profile.data.model.ProfileEditContentType
import org.codingforanimals.veganuniverse.commons.profile.data.remote.ProfileDTO
import org.codingforanimals.veganuniverse.commons.profile.data.storage.ProfileContent

internal fun ProfileDTO.toProfileContent(): List<ProfileContent> {
    val profileContent = mutableListOf<ProfileContent>()
    likes.recipes.forEach {
        val likedRecipe = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.RECIPE,
            actionType = ProfileEditActionType.LIKE,
        )
        profileContent.add(likedRecipe)
    }
    bookmarks.products.forEach {
        val bookmarkedProduct = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.PRODUCT,
            actionType = ProfileEditActionType.BOOKMARK,
        )
        profileContent.add(bookmarkedProduct)
    }
    bookmarks.places.forEach {
        val bookmarkedPlace = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.PLACE,
            actionType = ProfileEditActionType.BOOKMARK,
        )
        profileContent.add(bookmarkedPlace)
    }
    bookmarks.recipes.forEach {
        val bookmarkedRecipe = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.RECIPE,
            actionType = ProfileEditActionType.BOOKMARK,
        )
        profileContent.add(bookmarkedRecipe)
    }
    contributions.products.forEach {
        val contributedProduct = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.PRODUCT,
            actionType = ProfileEditActionType.CONTRIBUTION,
        )
        profileContent.add(contributedProduct)
    }
    contributions.recipes.forEach {
        val contributedRecipe = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.RECIPE,
            actionType = ProfileEditActionType.CONTRIBUTION,
        )
        profileContent.add(contributedRecipe)
    }
    contributions.places.forEach {
        val contributedPlace = ProfileContent(
            contentId = it.key,
            contentType = ProfileEditContentType.PLACE,
            actionType = ProfileEditActionType.CONTRIBUTION,
        )
        profileContent.add(contributedPlace)
    }
    return profileContent
}

internal fun List<ProfileContent>.toDomainModel(): Profile {
    val likedRecipes = mutableListOf<String>()
    val bookmarkedRecipes = mutableListOf<String>()
    val contributedRecipes = mutableListOf<String>()
    val bookmarkedPlaces = mutableListOf<String>()
    val contributedPlaces = mutableListOf<String>()
    val bookmarkedProducts = mutableListOf<String>()
    val contributedProducts = mutableListOf<String>()
    val contributedPlaceReviews = mutableListOf<String>()
    forEach {
        when (it.actionType) {
            ProfileEditActionType.LIKE -> when (it.contentType) {
                ProfileEditContentType.RECIPE -> likedRecipes
                ProfileEditContentType.PLACE -> null
                ProfileEditContentType.PRODUCT -> null
                ProfileEditContentType.PLACE_REVIEW -> null
            }

            ProfileEditActionType.BOOKMARK -> when (it.contentType) {
                ProfileEditContentType.RECIPE -> bookmarkedRecipes
                ProfileEditContentType.PLACE -> bookmarkedPlaces
                ProfileEditContentType.PRODUCT -> bookmarkedProducts
                ProfileEditContentType.PLACE_REVIEW -> null
            }

            ProfileEditActionType.CONTRIBUTION -> when (it.contentType) {
                ProfileEditContentType.RECIPE -> contributedRecipes
                ProfileEditContentType.PLACE -> contributedPlaces
                ProfileEditContentType.PRODUCT -> contributedProducts
                ProfileEditContentType.PLACE_REVIEW -> contributedPlaceReviews
            }
        }?.add(it.contentId)
    }
    return Profile(
        likes = ProfileLikes(
            recipes = likedRecipes,
        ),
        bookmarks = ProfileBookmarks(
            recipes = bookmarkedRecipes,
            places = bookmarkedPlaces,
            products = bookmarkedProducts,
        ),
        contributions = ProfileContributions(
            recipes = contributedRecipes,
            places = contributedPlaces,
            products = contributedProducts,
            placeReviews = contributedPlaceReviews,
        )
    )
}
