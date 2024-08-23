package org.codingforanimals.veganuniverse.product.list.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.product.list.presentation.model.Product
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.koin.androidx.compose.koinViewModel
import org.codingforanimals.veganuniverse.ui.R


@Composable
fun ProductRow(
    product: Product,
    onImageClick: () -> Unit,
    viewModel: ProductRowViewModel = koinViewModel(key = product.id),
) = with(product) {

    var additionalInfoVisible by rememberSaveable { mutableStateOf(false) }

    fun onProductClick() {
        additionalInfoVisible = !additionalInfoVisible
        viewModel.onAction(ProductRowViewModel.Action.OnProductClick(product))
    }

    Column(modifier = Modifier
        .animateContentSize()
        .clickable { onProductClick() },
        content = {
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(68.dp)
                        .clip(ShapeDefaults.Medium)
                        .clickable { onImageClick() },
                    model = product.imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing_02)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        product.type?.icon?.let {
                            VUIcon(
                                icon = it,
                                contentDescription = "",
                            )
                        }
                        Text(text = name, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(text = brand, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.weight(1f))

                val rotationState by animateFloatAsState(
                    targetValue = if (additionalInfoVisible) 180f else 0f,
                    label = "$name-$brand-chevron-rotation-value"
                )
                Icon(
                    modifier = Modifier
                        .rotate(rotationState)
                        .padding(horizontal = Spacing_05),
                    painter = painterResource(R.drawable.ic_chevron_down),
                    contentDescription = null,
                )

            }
            if (additionalInfoVisible) {
                ProductRowAdditionalInfo(viewModel.additionalInfo)
            }
            Divider()
        }
    )
}
