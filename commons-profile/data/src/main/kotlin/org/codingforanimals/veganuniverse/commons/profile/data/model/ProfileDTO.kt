package org.codingforanimals.veganuniverse.commons.profile.data.model

data class ProfileDTO(
    val likes: ProfileLikesDTO = ProfileLikesDTO(),
    val bookmarks: ProfileBookmarksDTO = ProfileBookmarksDTO(),
    val contributions: ProfileContributionsDTO = ProfileContributionsDTO(),
)

data class ProfileLikesDTO(
    val recipes: Map<String, Boolean> = emptyMap(),
)

data class ProfileBookmarksDTO(
    val recipes: Map<String, Boolean> = emptyMap(),
    val places: Map<String, Boolean> = emptyMap(),
    val products: Map<String, Boolean> = emptyMap(),
)

data class ProfileContributionsDTO(
    val recipes: Map<String, Boolean> = emptyMap(),
    val places: Map<String, Boolean> = emptyMap(),
    val products: Map<String, Boolean> = emptyMap(),
)
