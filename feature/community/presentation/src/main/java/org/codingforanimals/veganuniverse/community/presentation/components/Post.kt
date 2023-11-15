package org.codingforanimals.veganuniverse.community.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_07
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun Post(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String? = null,
    image: Boolean = false,
    imageUrl: String? = null,
    onClick: () -> Unit = {},
    details: @Composable (() -> Unit)? = null,
) {
    Card(
        modifier = modifier.clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Image(
                modifier = Modifier
                    .padding(top = Spacing_04, start = Spacing_04)
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.vegan_restaurant),
                contentDescription = "Imágen del usuario creador del post",
            )
            Column(modifier = Modifier.padding(start = Spacing_04, top = Spacing_04)) {
                Text(text = title, fontWeight = FontWeight.Medium, maxLines = 2)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
            Spacer(Modifier.weight(1f))
            VUIcon(
                icon = VUIcons.MoreOptions,
                contentDescription = "Más opciones",
                onIconClick = {},
            )
        }

        details?.let {
            Row(
                modifier = Modifier.padding(
                    start = Spacing_04,
                    end = Spacing_04,
                    top = Spacing_04,
                )
            ) {
                details()
            }
        }

        description?.let {
            Text(
                modifier = Modifier.padding(top = Spacing_04, start = Spacing_04, end = Spacing_07),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (image || imageUrl != null) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
                    .padding(Spacing_04)
                    .clip(ShapeDefaults.Small),
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Spacing_02),
        ) {
            VUAssistChip(
                icon = VUIcons.Favorite,
                label = "200",
                onClick = {},
                iconDescription = "Me gusta",
                colors = VUAssistChipDefaults.secondaryColors(),
            )
            VUAssistChip(
                icon = VUIcons.Comment,
                label = "50",
                onClick = {},
                iconDescription = "Comentar",
                colors = VUAssistChipDefaults.secondaryColors(),
            )
            VUIcon(
                icon = VUIcons.Share,
                contentDescription = "Compartir",
                onIconClick = {},
            )
            Spacer(Modifier.weight(1f))
            VUIcon(
                icon = VUIcons.Bookmark,
                contentDescription = "Guardar",
                onIconClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPost() {
    VeganUniverseTheme {
        Post(
            title = "Titulo",
            subtitle = "Subtitulo",
            description = "Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, Descripción, ",
        )
    }
}