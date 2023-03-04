@file:OptIn(ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.presentation.R

@Composable
fun RecipesHomeScreen(
    navigateToCategoryRecipes: () -> Unit,
) {
    val spacing = Spacing_04
    val textHeight = 50.dp
    val columnWidth = (LocalConfiguration.current.screenWidthDp - (3 * spacing.value)) / 2
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        content = {
            itemsIndexed(items = list, span = { _, item ->
                when (item) {
                    is Header -> StaggeredGridItemSpan.FullLine
                    else -> StaggeredGridItemSpan.SingleLane
                }
            }, itemContent = { index, item ->
                when (item) {
                    is Header -> {
                        val modifier = if (index != 0) {
                            Modifier.padding(top = Spacing_04)
                        } else Modifier
                        Text(
                            modifier = modifier,
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    is Content -> {
                        val cardHeight = if (item.doubleSpan) {
                            (2 * columnWidth).dp + (spacing.value).dp
                        } else {
                            columnWidth.dp
                        }
                        Card(
                            modifier = Modifier
                                .height(cardHeight)
                                .clickable { navigateToCategoryRecipes() },
                        ) {
                            AsyncImage(
                                model = item.image,
                                modifier = Modifier
                                    .height(cardHeight - textHeight)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Crop,
                                contentDescription = ""
                            )
                            Text(
                                modifier = Modifier
                                    .height(textHeight)
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                text = item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            })
        })
}

val list = listOf(
    Header("Desayunos cargados"),
    Content("Granolas", R.drawable.test_img_granola, false),
    Content("Panificados", R.drawable.test_img_panificados, true),
    Content("Postres", R.drawable.test_img_postres, false),
    Content("Galletitas", R.drawable.test_img_galletitas, false),
    Content("Facturas", R.drawable.test_img_facturas, false),
    Header("Meriendas livianas"),
    Content("Postres", R.drawable.test_img_postres, false),
    Content("Granolas", R.drawable.test_img_granola, false),
    Content("Panificados", R.drawable.test_img_panificados, true),
    Content("Galletitas", R.drawable.test_img_galletitas, false),
    Content("Facturas", R.drawable.test_img_facturas, false),
    Header("Almuerzos en 15 minutos"),
    Content("Granolas", R.drawable.test_img_granola, false),
    Content("Panificados", R.drawable.test_img_panificados, true),
    Content("Postres", R.drawable.test_img_postres, true),
    Content("Galletitas", R.drawable.test_img_galletitas, false),
    Content("Facturas", R.drawable.test_img_facturas, false),
)

sealed class CategoryScreenItem

data class Header(
    val title: String,
) : CategoryScreenItem()

data class Content(
    val name: String,
    val image: Int,
    val doubleSpan: Boolean,
)