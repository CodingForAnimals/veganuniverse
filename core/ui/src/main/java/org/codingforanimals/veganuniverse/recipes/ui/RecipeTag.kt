package org.codingforanimals.veganuniverse.recipes.ui

import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

enum class RecipeTag(val label: Int, val icon: Icon) {
    GLUTEN_FREE(R.string.recipe_tag_label_gluten_free, VUIcons.GlutenFree),
    LOW_SODIUM(R.string.recipe_tag_label_low_sodium, VUIcons.SaltFree),
    SUGAR_FREE(R.string.recipe_tag_label_sugar_free, VUIcons.SugarFree),
    NOT_DAIRY(R.string.recipe_tag_label_not_dairy, VUIcons.DairyFree),
    LUNCH_AND_DINNER(R.string.recipe_tag_label_lunch_and_dinner, VUIcons.Restaurant),
    SALTY(R.string.recipe_tag_label_salty, VUIcons.Shaker),
    NIBBLES(R.string.recipe_tag_label_nibbles, VUIcons.Nibbles),
    BREAKFAST_AND_EVENING(R.string.recipe_tag_label_breakfast_and_dinner, VUIcons.CoffeeMug),
    QUICK_RECIPE(R.string.recipe_tag_label_quick_recipe, VUIcons.Clock),
    SWEET(R.string.recipe_tag_label_sweet, VUIcons.Pie),
    DRESSING(R.string.recipe_tag_label_dressing, VUIcons.Dressing),
    CANNED(R.string.recipe_tag_label_canned, VUIcons.Jar),
}