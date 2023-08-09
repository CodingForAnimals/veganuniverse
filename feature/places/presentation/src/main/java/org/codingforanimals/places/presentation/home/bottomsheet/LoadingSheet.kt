package org.codingforanimals.places.presentation.home.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04

private fun Modifier.loadingPlaceholder() = composed {
    placeholder(
        visible = true,
        color = MaterialTheme.colorScheme.surface,
        highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
internal fun LoadingSheet() {
    Column {
        Row(
            modifier = Modifier.padding(horizontal = Spacing_04),
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            VUAssistChip(
                modifier = Modifier
                    .clip(CircleShape)
                    .loadingPlaceholder(),
                label = "Filtrar",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
            )
            VUAssistChip(
                modifier = Modifier
                    .clip(CircleShape)
                    .loadingPlaceholder(),
                label = "Ordenar",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
            )
        }
        repeat(3) {
            Column(
                modifier = Modifier.padding(Spacing_04),
                verticalArrangement = Arrangement.spacedBy(Spacing_04)
            ) {
                Box(
                    modifier = Modifier
                        .clip(ShapeDefaults.Medium)
                        .loadingPlaceholder()
                        .height(200.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}