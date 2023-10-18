package org.codingforanimals.veganuniverse.create.presentation.recipe.usecase

sealed class SubmitRecipeStatus {
    data object Loading : SubmitRecipeStatus()
    data object Error : SubmitRecipeStatus()
    data object UnauthorizedUser : SubmitRecipeStatus()
    data object EmailNotVerified : SubmitRecipeStatus()
    data object Success : SubmitRecipeStatus()
}