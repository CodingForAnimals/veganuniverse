@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.commons.ui.contentdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon

@Composable
fun FeatureItemScreenTagsFlowRow(
    modifier: Modifier = Modifier,
    tags: List<TagItem>,
) {
    val isListOddNumbered = remember { tags.size.rem(2) == 1 }
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        tags.forEachIndexed { index, tag ->
            key(index) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = Spacing_04),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    VUIcon(
                        icon = tag.icon,
                        contentDescription = stringResource(tag.label),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(tag.label),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        if (isListOddNumbered) Spacer(modifier = Modifier.weight(1f))
    }
}

data class TagItem(
    val icon: Icon,
    val label: Int,
)