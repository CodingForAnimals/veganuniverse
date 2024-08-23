package org.codingforanimals.veganuniverse.commons.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_07

@Composable
fun ContentDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: Int,
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            modifier = Modifier.padding(start = Spacing_07),
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun ContentDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: @Composable () -> Unit,
    icon: Int? = null,
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(
            modifier = Modifier
                .padding(start = Spacing_07)
                .takeIf { icon != null } ?: Modifier.padding(top = Spacing_03),
        ) {
            subtitle()
        }
    }
}