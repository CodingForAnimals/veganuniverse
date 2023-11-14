package org.codingforanimals.veganuniverse.recipes.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.codingforanimals.veganuniverse.ui.Spacing_01
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.Icon

@Composable
internal fun RecipesHomeItemHeader(
    modifier: Modifier = Modifier,
    icon: Icon,
    label: String,
    buttonLabel: String,
    onButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(start = Spacing_02),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing_04)
        ) {
            VUIcon(
                icon = icon,
                contentDescription = label,
            )
            Text(
                modifier = Modifier.padding(top = Spacing_01),
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
//        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            modifier = Modifier.widthIn(min = ButtonDefaults.MinWidth), onClick = onButtonClick
        ) {
            Text(
                text = buttonLabel,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}