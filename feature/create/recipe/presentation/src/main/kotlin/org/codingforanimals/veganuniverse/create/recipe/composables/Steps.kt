package org.codingforanimals.veganuniverse.create.recipe.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
internal fun Steps(
    stepsField: StringListField,
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
            !stepsField.isValid -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Text(
            text = stringResource(R.string.steps),
            style = MaterialTheme.typography.titleLarge,
            color = color,
        )
        stepsField.list.forEachIndexed { index, step ->
            key(index) {
                Row(
                    modifier = Modifier.animateAlphaOnStart(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_05),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.width(18.dp),
                        text = "${index + 1}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    val imeAction = if (index == stepsField.list.lastIndex) ImeAction.Done else ImeAction.Next
                    VUNormalTextField(
                        modifier = Modifier.weight(1f),
                        value = step,
                        onValueChange = { onAction(Action.OnStepChange.Text(index, it)) },
                        placeholder = if (index == 0) stringResource(R.string.create_recipe_steps_placeholder) else null,
                        maxChars = 128,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = imeAction,
                        ),
                    )
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            VUIcon(
                                icon = VUIcons.MoreOptions,
                                contentDescription = stringResource(R.string.create_recipe_delete_ingredient),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.create_recipe_add_next_step)) },
                                onClick = {
                                    onAction(Action.OnStepChange.AddAt(index))
                                    expanded = false
                                },
                                leadingIcon = {
                                    VUIcon(
                                        icon = VUIcons.Add,
                                        contentDescription = stringResource(R.string.create_recipe_add_next_step)
                                    )
                                },
                            )
                            if (stepsField.list.size > 1) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.create_recipe_remove_step)) },
                                    onClick = {
                                        onAction(Action.OnStepChange.Delete(index))
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        VUIcon(
                                            icon = VUIcons.Delete,
                                            contentDescription = stringResource(R.string.create_recipe_remove_step)
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = Spacing_05),
            onClick = { onAction(Action.OnStepChange.Add) },
        ) {
            VUIcon(
                modifier = Modifier.padding(end = Spacing_04),
                icon = VUIcons.Create,
                contentDescription = stringResource(R.string.create_recipe_add_step),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.step),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}