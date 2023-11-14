package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.icon.Icon

@Composable
fun VURadioButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: Icon? = null,
    paddingValues: PaddingValues? = null,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    val paddingModifier = paddingValues?.let { Modifier.padding(it) } ?: Modifier
    Row(
        modifier = modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .then(paddingModifier),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = colors,
        )
        icon?.let {
            VUIcon(
                icon = icon,
                contentDescription = "",
            )
        }
        Text(text = label)
    }
}