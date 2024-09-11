@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.recipes.presentation.R

@Composable
internal fun AllRecipeTags(
    modifier: Modifier = Modifier,
    onRecipeTagClick: (RecipeTag) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Text(
            text = stringResource(R.string.all_tags),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
        )
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            RecipeTag.entries
                .forEachIndexed { index, recipeTag ->
                    key(index) {
                        val recipeTagUI = remember { recipeTag.toUI() }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onRecipeTagClick(recipeTag) },
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxHeight(0.33f)
                                            .aspectRatio(1f),
                                        model = recipeTagUI.icon.model,
                                        contentScale = ContentScale.Fit,
                                        contentDescription = stringResource(recipeTagUI.label),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .fillMaxWidth()
                                            .padding(Spacing_02),
                                        text = stringResource(recipeTagUI.label),
                                        maxLines = 2,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }
}
