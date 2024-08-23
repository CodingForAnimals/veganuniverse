package org.codingforanimals.veganuniverse.commons.profile.domain.model

data class Profile(
    val likes: ProfileLikes,
    val bookmarks: ProfileBookmarks,
    val contributions: ProfileContributions,
)

data class ProfileLikes(
    val recipes: List<String>,
)

data class ProfileBookmarks(
    val recipes: List<String>,
    val places: List<String>,
    val products: List<String>,
)

data class ProfileContributions(
    val recipes: List<String>,
    val places: List<String>,
    val products: List<String>,
    val placeReviews: List<String>,
)
