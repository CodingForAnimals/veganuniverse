package org.codingforanimals.veganuniverse.product.presentation.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.PrimaryUltraLight
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.drawable.ic_chevron_right
import org.codingforanimals.veganuniverse.product.presentation.R

@Composable
internal fun SeeAdditivesBanner(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, CardDefaults.shape),
        onClick = { onClick?.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = PrimaryUltraLight,
        ),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Spacing_06,
                vertical = Spacing_05,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.product_see_additives),
                style = MaterialTheme.typography.titleMedium,
            )
            onClick?.let {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(ic_chevron_right),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdditivesBanner() {
    VeganUniverseTheme {
        Surface {
            SeeAdditivesBanner(
                modifier = Modifier
                    .padding(Spacing_06),
                onClick = {},
            )
        }
    }
}
