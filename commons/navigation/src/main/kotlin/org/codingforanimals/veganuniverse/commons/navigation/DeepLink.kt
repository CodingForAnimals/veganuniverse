package org.codingforanimals.veganuniverse.commons.navigation

sealed class DeepLink(val value: String) {
    data object AuthPrompt : DeepLink("auth_prompt")
    data object ValidateEmailPrompt : DeepLink("validate_email_prompt")
    data object EmailValidated : DeepLink("email_validated")

    data object CreatePlace : DeepLink("create_place")
    data object CreateProduct : DeepLink("create_product")
    data object CreateRecipe : DeepLink("create_recipe")

    val deeplink: String
        get() = getDeeplink(value)

    private fun getDeeplink(value: String): String {
        return "$SCHEMA$value"
    }

    companion object {
        private const val SCHEMA = "veganuniverse://"
    }
}
