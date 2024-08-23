package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
fun CreateProductCTA(
    onButtonClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.add_product_suggestion),
        style = MaterialTheme.typography.bodyMedium
    )
    VUAssistChip(
        onClick = onButtonClick,
        label = stringResource(R.string.go_to_add_product),
        icon = VUIcons.ArrowForward,
        chipElevation = VUAssistChipDefaults.elevatedAssistChipElevation(),
    )
}