package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.commons.ui.R.drawable.ic_chevron_down
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar

@Composable
fun ProductRow(
    product: Product,
    onImageClick: () -> Unit,
    onEditClick: () -> Unit,
    onReportClick: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    onSnackbarEffect: (Snackbar) -> Unit,
) = with(product) {

    var additionalInfoVisible by rememberSaveable { mutableStateOf(false) }

    fun onProductClick() {
        additionalInfoVisible = !additionalInfoVisible
    }

    Column(modifier = Modifier
        .animateContentSize()
        .clickable { onProductClick() },
        content = {
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val imageModifier = Modifier
                    .padding(
                        start = Spacing_06,
                        end = Spacing_05,
                        top = Spacing_05,
                        bottom = Spacing_05,
                    )
                    .size(68.dp)
                    .clip(ShapeDefaults.Medium)
                product.imageUrl?.let { imageUrl ->
                    AsyncImage(
                        modifier = imageModifier.clickable { onImageClick() },
                        model = imageUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                } ?: Box(
                    modifier = imageModifier.background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.no_image),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }

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
                    label = "${product.id}-chevron-rotation-value"
                )
                Icon(
                    modifier = Modifier
                        .padding(start = Spacing_05, end = Spacing_06)
                        .rotate(rotationState),
                    painter = painterResource(ic_chevron_down),
                    contentDescription = null,
                )

            }
            AnimatedVisibility(visible = additionalInfoVisible) {
                ProductAdditionalInfo(
                    product = product,
                    onEditClick = onEditClick,
                    onReportClick = onReportClick,
                    onSnackbarEffect = onSnackbarEffect,
                    navigateToAuthenticateScreen = navigateToAuthenticateScreen
                )
            }
            HorizontalDivider()
        }
    )
}
