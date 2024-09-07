package org.codingforanimals.veganuniverse.commons.product.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.LightBlue
import org.codingforanimals.veganuniverse.commons.designsystem.LightGray
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.R
import org.codingforanimals.veganuniverse.commons.product.presentation.resolveBrand
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import java.util.Date

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(Spacing_05),
        ) {
            Row(
                modifier = Modifier.height(70.dp),
                horizontalArrangement = Arrangement.spacedBy(Spacing_05),
            ) {
                ProductCardImage(
                    category = product.category,
                    name = product.name,
                    imageUrl = product.imageUrl
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing_02)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = product.resolveBrand,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                val typeUI = remember { product.type.toUI() }
                with(typeUI) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = icon.id),
                        contentDescription = stringResource(id = label),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

sealed class ProductCardImage {
    data class TextImage(val text: String, val backgroundColor: Color) : ProductCardImage()
    data class Image(val url: String?) : ProductCardImage()
}

@Composable
private fun ProductCardImage(
    category: ProductCategory,
    name: String,
    imageUrl: String?
) {
    val image = remember {
        when (category) {
            ProductCategory.ADDITIVES -> ProductCardImage.TextImage(
                text = name,
                backgroundColor = LightBlue
            )

            ProductCategory.OTHER -> {
                imageUrl?.let { ProductCardImage.Image(it) } ?: ProductCardImage.TextImage(
                    text = name,
                    backgroundColor = LightGray
                )
            }

            else -> {
                ProductCardImage.Image(imageUrl)
            }
        }
    }
    when (image) {
        is ProductCardImage.TextImage -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CardDefaults.shape)
                    .background(image.backgroundColor)
            ) {
                Text(
                    modifier = Modifier
                        .padding(Spacing_03)
                        .align(Alignment.Center),
                    text = image.text,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }

        is ProductCardImage.Image -> {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CardDefaults.shape),
                model = imageUrl,
                contentDescription = stringResource(id = R.string.product_image)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewProductCard() {
    VeganUniverseTheme {
        Scaffold { paddingValues ->
            val product = Product(
                id = "123",
                name = "Producto Pepe",
                brand = "Argento's",
                comment = "Rico y económico. 100% vegano. Recomiendo!!",
                type = ProductType.VEGAN,
                category = ProductCategory.BAKED_GOODS,
                userId = "123123",
                username = "Paola Argento",
                imageUrl = null,
                createdAt = Date(),
                validated = true,
            )
            val additive = product.copy(
                name = "INS-311",
                category = ProductCategory.ADDITIVES,
            )
            val other = product.copy(
                category = ProductCategory.OTHER
            )
            val otherWithImage = other.copy(
                imageUrl = "image_url"
            )
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(Spacing_05),
                contentPadding = PaddingValues(Spacing_05)
            ) {
                items(listOf(product, additive, other, otherWithImage)) {
                    ProductCard(product = it)
                }
            }
        }
    }
}
