package org.codingforanimals.veganuniverse.recipes.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import java.util.Date

@Composable
internal fun RecipeCard(
    modifier: Modifier = Modifier,
    recipe: Recipe,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .padding(bottom = Spacing_04),
            model = recipe.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Row(
            modifier = Modifier.padding(horizontal = Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${recipe.likes} me gusta",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            modifier = Modifier.padding(
                start = Spacing_04,
                end = Spacing_04,
                bottom = Spacing_04,
                top = Spacing_02,
            ),
            text = recipe.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun PreviewRecipeCard() {
    VeganUniverseTheme {
        RecipeCard(
            recipe = Recipe(
                id = "123",
                userId = "123",
                username = "El pepe",
                name = "Receta de Pepe. Receta de Pepe. Receta de Pepe. Receta de Pepe. Receta de Pepe. Receta de Pepe. Receta de Pepe. Receta de Pepe.",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                likes = 3,
                createdAt = Date(),
                tags = listOf(RecipeTag.QUICK_RECIPE, RecipeTag.SWEET),
                ingredients = listOf("Ingrediente 1", "Ingrediente 2"),
                steps = listOf("Paso 1", "Paso 2"),
                prepTime = "15 min",
                servings = "8 porciones",
                imageUrl = null,
                validated = true,
            ),
            onClick = {}
        )
    }
}
