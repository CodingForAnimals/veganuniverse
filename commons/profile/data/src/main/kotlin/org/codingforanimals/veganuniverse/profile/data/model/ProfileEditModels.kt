package org.codingforanimals.veganuniverse.profile.data.model

enum class ProfileEditActionValue {
    ADD, REMOVE;
}

enum class ProfileEditContentType {
    RECIPE, PLACE, PRODUCT,
}

enum class ProfileEditActionType {
    LIKE, BOOKMARK, CONTRIBUTION,
}

data class ProfileEditArguments(
    val userId: String,
    val contentId: String,
    val profileEditContentType: ProfileEditContentType,
    val profileEditActionType: ProfileEditActionType,
    val profileEditActionValue: ProfileEditActionValue,
)
