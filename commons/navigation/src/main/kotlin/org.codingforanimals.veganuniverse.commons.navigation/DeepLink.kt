package org.codingforanimals.veganuniverse.commons.navigation

sealed class DeepLink(val value: String) {
    data object Reauthentication : DeepLink("reauthentication")
    data object AuthPrompt : DeepLink("auth_prompt")
    data object ValidateEmailPrompt : DeepLink("validate_email_prompt")

    data object CreatePlace : DeepLink("create_place")
    data object CreateProduct : DeepLink("create_product")
    data object CreateRecipe : DeepLink("create_recipe")
    data object ThankYou : DeepLink("thank_you")

    val deeplink: String
        get() = getDeeplink(value)

    private fun getDeeplink(value: String): String {
        return "$SCHEMA$value"
    }

    companion object {
        private const val SCHEMA = "veganuniverse://"
    }
}
