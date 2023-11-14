package org.codingforanimals.veganuniverse.create.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04

@Composable
internal fun BottomPageScroller(
    modifier: Modifier = Modifier,
    index: Int,
    onItemClick: (Int) -> Unit,
) {
    ScrollableTabRow(
        modifier = modifier.padding(bottom = Spacing_04),
        selectedTabIndex = index,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        divider = {},
        indicator = {},
    ) {
        products.forEachIndexed { currentIndex, screenId ->
            val shape = when (currentIndex) {
                0 -> RoundedCornerShape(bottomStart = 15.dp, topStart = 15.dp)
                products.lastIndex -> RoundedCornerShape(bottomEnd = 15.dp, topEnd = 15.dp)
                else -> RectangleShape
            }
            TextButton(
                onClick = { onItemClick(currentIndex) },
                colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = shape,
            ) {
                val weight =
                    if (index == currentIndex) FontWeight.Bold else FontWeight.Normal
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(min = 80.dp)
                        .padding(horizontal = Spacing_02),
                    text = screenId.name,
                    textAlign = TextAlign.Center,
                    fontWeight = weight,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

internal val products = listOf(
    PostScreenId(),
    PlaceScreenId(),
    ProductScreenId(),
    RecipeScreenId(),
    OtherScreenId(),
)

sealed class CreateScreenId(val name: String)

class PostScreenId : CreateScreenId("Publicación")
class PlaceScreenId : CreateScreenId("Lugar")
class ProductScreenId : CreateScreenId("Producto")
class RecipeScreenId : CreateScreenId("Receta")
class OtherScreenId : CreateScreenId("Otro")
