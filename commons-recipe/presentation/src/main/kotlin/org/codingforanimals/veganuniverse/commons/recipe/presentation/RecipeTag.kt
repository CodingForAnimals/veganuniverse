package org.codingforanimals.veganuniverse.commons.recipe.presentation

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

data class RecipeTagUI(
    @StringRes val label: Int,
    val icon: Icon,
)

fun RecipeTag.toUI(): RecipeTagUI {
    return when (this) {
        RecipeTag.GLUTEN_FREE -> RecipeTagUI(
            R.string.recipe_tag_label_gluten_free,
            VUIcons.GlutenFree
        )

        RecipeTag.LOW_SODIUM -> RecipeTagUI(R.string.recipe_tag_label_low_sodium, VUIcons.SaltFree)
        RecipeTag.SUGAR_FREE -> RecipeTagUI(R.string.recipe_tag_label_sugar_free, VUIcons.SugarFree)
        RecipeTag.NOT_DAIRY -> RecipeTagUI(R.string.recipe_tag_label_not_dairy, VUIcons.DairyFree)
        RecipeTag.LUNCH_AND_DINNER -> RecipeTagUI(
            R.string.recipe_tag_label_lunch_and_dinner,
            VUIcons.Restaurant
        )

        RecipeTag.SALTY -> RecipeTagUI(R.string.recipe_tag_label_salty, VUIcons.Shaker)
        RecipeTag.NIBBLES -> RecipeTagUI(R.string.recipe_tag_label_nibbles, VUIcons.Nibbles)
        RecipeTag.BREAKFAST_AND_EVENING -> RecipeTagUI(
            R.string.recipe_tag_label_breakfast_and_dinner, VUIcons.CoffeeMug
        )

        RecipeTag.QUICK_RECIPE -> RecipeTagUI(R.string.recipe_tag_label_quick_recipe, VUIcons.Clock)
        RecipeTag.SWEET -> RecipeTagUI(R.string.recipe_tag_label_sweet, VUIcons.Pie)
        RecipeTag.DRESSING -> RecipeTagUI(R.string.recipe_tag_label_dressing, VUIcons.Dressing)
        RecipeTag.CANNED -> RecipeTagUI(R.string.recipe_tag_label_canned, VUIcons.Jar)
    }
}
