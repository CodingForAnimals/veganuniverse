package org.codingforanimals.veganuniverse.create.recipe.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel.Action
import org.codingforanimals.veganuniverse.create.recipe.model.StringListField
import org.codingforanimals.veganuniverse.create.recipe.presentation.R
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUNormalTextField
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons


@Composable
internal fun Ingredients(
    ingredientsField: StringListField,
    isValidating: Boolean,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing_06)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing_05)
    ) {
        val color = when {
            !isValidating -> MaterialTheme.colorScheme.onSurfaceVariant
            !ingredientsField.isValid -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Text(
            text = stringResource(R.string.ingredients),
            style = MaterialTheme.typography.titleLarge,
            color = color,
        )
        ingredientsField.list.forEachIndexed { index, ingredient ->
            key(index) {
                Row(
                    modifier = Modifier.animateAlphaOnStart(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_05),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    VUNormalTextField(
                        modifier = Modifier.weight(1f),
                        value = ingredient,
                        onValueChange = { onAction(Action.OnIngredientChange.Text(index, it)) },
                        placeholder = if (index == 0) stringResource(R.string.create_recipe_ingredient_placeholder) else null,
                        maxChars = 600,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next,
                        ),
                    )
                    IconButton(
                        onClick = { onAction(Action.OnIngredientChange.Delete(index)) },
                        enabled = ingredientsField.list.size > 1,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.outline,
                        )
                    ) {
                        VUIcon(
                            icon = VUIcons.Delete,
                            contentDescription = stringResource(R.string.create_recipe_delete_ingredient),
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = Spacing_05),
            onClick = { onAction(Action.OnIngredientChange.Add) },
        ) {
            VUIcon(
                modifier = Modifier.padding(end = Spacing_04),
                icon = VUIcons.Create,
                contentDescription = stringResource(R.string.create_recipe_add_ingredient),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.ingredient),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}