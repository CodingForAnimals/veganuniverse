package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.commons.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.commons.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun CreateProductCTA(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
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
}