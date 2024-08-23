@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.commons.ui.contentdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.R
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
fun FeatureItemScreenTagsFlowRow(
    modifier: Modifier = Modifier,
    tags: List<TagItem>,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = VUIcons.Comment.id),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.tags),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        FlowRow(
            modifier = modifier.fillMaxWidth().padding(start = Spacing_06),
            maxItemsInEachRow = 2,
        ) {
            tags.forEachIndexed { index, tag ->
                key(index) {
                    val isSingleTagRow =
                        remember { (index == (tags.size - 1)) && (index.rem(2) == 0) }
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
                        )
                    }
                    if (isSingleTagRow) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

data class TagItem(
    val icon: Icon,
    val label: Int,
)