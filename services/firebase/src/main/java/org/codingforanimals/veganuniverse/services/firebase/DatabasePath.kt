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

    object Profile {
        object Likes {
            const val RECIPES = "profile/likes/recipes"
            const val PLACES = "profile/likes/places"
        }

        object Bookmarks {
            const val RECIPES = "profile/bookmarks/recipes"
            const val PLACES = "profile/bookmarks/places"
        }

        object Contributions {
            const val RECIPES = "profile/contributions/recipes"
            const val PLACES = "profile/contributions/places"
        }
    }
}