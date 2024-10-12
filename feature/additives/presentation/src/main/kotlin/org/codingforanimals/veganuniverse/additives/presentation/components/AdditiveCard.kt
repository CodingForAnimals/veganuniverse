package org.codingforanimals.veganuniverse.additives.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.additives.domain.model.Additive
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.additives.presentation.model.AdditiveTypeUI
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme


@Composable
internal fun AdditiveCard(
    additive: Additive,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val typeUI = remember { AdditiveTypeUI.fromString(additive.type.name) }
    Card(
        modifier = modifier,
        border = BorderStroke(
            width = 3.dp,
            color = typeUI.color.copy(alpha = 0.5f),
        ),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = typeUI.color.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing_05),
            verticalArrangement = Arrangement.spacedBy(Spacing_02)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = additive.code,
                    style = MaterialTheme.typography.titleMedium,
                )
                with(typeUI) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = icon.id),
                        contentDescription = stringResource(id = label),
                        tint = Color.Unspecified
                    )
                }
            }
            additive.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAdditiveCard() {
    VeganUniverseTheme {
        Surface {
            val additive = Additive(
                id = "123",
                code = "INS 100",
                name = "Curcumina",
                description = "Colorante amarillo derivado de la cúrcuma",
                type = AdditiveType.VEGAN,
            )
            val additives = listOf(
                additive,
                additive.copy(
                    id = "1234", type = AdditiveType.NOT_VEGAN
                ),
                additive.copy(
                    id = "1235", type = AdditiveType.DOUBTFUL, name = null
                ),
                additive.copy(
                    id = "1236", type = AdditiveType.UNKNOWN, description = null
                ),
            )
            Column(
                modifier = Modifier.padding(vertical = Spacing_05),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                additives.forEach {
                    AdditiveCard(
                        modifier = Modifier.padding(horizontal = Spacing_05),
                        additive = it,
                        onClick = {},
                    )
                }
            }
        }
    }
}
