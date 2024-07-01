package org.codingforanimals.veganuniverse.product.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.animation.ShimmerItem

@Composable
fun ColumnScope.ProductRowLoading() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerItem(
            modifier = Modifier
                .padding(
                    start = Spacing_06,
                    top = Spacing_05,
                    end = Spacing_05,
                    bottom = Spacing_05
                )
                .size(68.dp)
                .clip(ShapeDefaults.Medium)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing_03)
        ) {
            ShimmerItem(
                Modifier
                    .fillMaxWidth(0.5f)
                    .height(20.dp)
                    .clip(ShapeDefaults.Small)
            )
            ShimmerItem(
                Modifier
                    .fillMaxWidth(0.3f)
                    .height(20.dp)
                    .clip(ShapeDefaults.Small)
            )
        }
    }
    HorizontalDivider()
}