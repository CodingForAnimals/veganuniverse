package org.codingforanimals.veganuniverse.community.model

data class UserPost(
    val userImage: Int,
    val username: String,
    val title: String,
    val category: String,
    val text: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isBookmarked: Boolean,
)