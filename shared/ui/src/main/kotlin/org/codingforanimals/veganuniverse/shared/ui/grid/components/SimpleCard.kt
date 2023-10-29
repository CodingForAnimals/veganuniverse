package org.codingforanimals.veganuniverse.shared.ui.grid.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.shared.ui.grid.model.SimpleCardLayoutType

@Composable
internal fun SimpleCard(
    modifier: Modifier = Modifier,
    title: String,
    imageRef: String,
    layoutType: SimpleCardLayoutType,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .then(layoutType.modifier)
            .clickable(onClick = onClick)
            .shadow(elevation = 10.dp, shape = ShapeDefaults.Small),
    ) {
        AsyncImage(
            model = imageRef,
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Crop,
            contentDescription = title,
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .heightIn(min = 50.dp)
                .fillMaxWidth()
                .padding(vertical = Spacing_04),
            text = title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
