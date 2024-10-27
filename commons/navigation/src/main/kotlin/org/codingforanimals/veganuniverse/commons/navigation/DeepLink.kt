package org.codingforanimals.veganuniverse.commons.navigation

sealed class DeepLink(private val value: String) {
    data object Validator : DeepLink("validator")
    data object AuthPrompt : DeepLink("auth_prompt")
    data object ValidateEmailPrompt : DeepLink("validate_email_prompt")
    data object EmailValidated : DeepLink("email_validated")

    data object CreatePlace : DeepLink("create_place")
    data object CreateProduct : DeepLink("create_product")
    data object CreateRecipe : DeepLink("create_recipe")
    data object CreateAdditive : DeepLink("create_additive")
    data class CreateThankYou(val feature: String) : DeepLink("$PATH/$feature") {
        companion object {
            private const val PATH = "create_thank_you"
            val pathWithSchema = withSchema(PATH)
        }
    }

    data class PlaceDetail(val id: String) : DeepLink("$PATH/$id") {
        companion object {
            private const val PATH = "place_detail"
            val pathWithSchema = withSchema(PATH)
        }
    }

    val deeplink: String
        get() = withSchema(value)

    companion object {
        private const val SCHEMA = "veganuniverse://"
        const val APP_LINKS_BASE_URL = BuildConfig.APP_LINKS_BASE_URL
        fun withSchema(value: String): String {
            return "$SCHEMA$value"
        }
    }
}
