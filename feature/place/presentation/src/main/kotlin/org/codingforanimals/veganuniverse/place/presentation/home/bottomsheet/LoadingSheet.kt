package org.codingforanimals.veganuniverse.place.presentation.home.bottomsheet

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
internal fun LoadingSheet() {
    Column {
        Row(
            modifier = Modifier.padding(horizontal = Spacing_04),
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            VUAssistChip(
                modifier = Modifier.clip(CircleShape),
                label = "Filtrar",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
            )
            VUAssistChip(
                modifier = Modifier
                    .clip(CircleShape),
                label = "Ordenar",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
            )
        }
        repeat(3) {
            ShimmerItem(Modifier
                .padding(Spacing_04)
                .clip(ShapeDefaults.Medium)
                .height(200.dp)
                .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLoadingSheet() {
    VeganUniverseTheme {
        LoadingSheet()
    }
}
