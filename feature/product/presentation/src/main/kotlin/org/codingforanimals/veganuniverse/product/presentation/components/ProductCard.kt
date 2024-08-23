package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import java.util.Date

@Composable
internal fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
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
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(CardDefaults.shape)
                        .clickable { onImageClick() },
                    model = product.imageUrl,
                    contentDescription = stringResource(id = R.string.product_image)
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
                        text = product.brand,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                product.type?.let { type ->
                    VUIcon(
                        icon = type.icon,
                        contentDescription = stringResource(id = type.label)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewProductCard() {
    VeganUniverseTheme {
        Scaffold { paddingValues ->
            val product = Product(
                "123",
                "Producto Pepe",
                "Argento's",
                "Rico y económico. 100% vegano. Recomiendo!!",
                ProductType.VEGAN.toUI(),
                ProductCategory.BAKED_GOODS.toUI(),
                "123123",
                "Paola Argento",
                null,
                Date(),
            )
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(Spacing_05),
                contentPadding = PaddingValues(Spacing_05)
            ) {
                items(listOf(product, product, product)) {
                    ProductCard(product = it)
                }
            }
        }
    }
}
