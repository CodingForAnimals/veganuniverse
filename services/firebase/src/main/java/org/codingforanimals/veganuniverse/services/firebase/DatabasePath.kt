package org.codingforanimals.veganuniverse.services.firebase

object DatabasePath {
    object Content {
        object Places {
            fun card(placeId: String) = "content/places/cards/$placeId"
            const val GEO_FIRE = "content/places/geofire"
        }

        object Recipes {
            const val USER_RECIPES = "content/recipes/userRecipes"
            fun userRecipesLookup(userId: String): String {
                return "$USER_RECIPES/$userId"
            }
        }
    }
}