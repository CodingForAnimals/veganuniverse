@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.community

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_07
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme


@Composable
fun Post(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String?,
    image: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.clickable { onClick() },
    ) {
        Header(
            title = title,
            subtitle = subtitle,
        )
        Description(
            description = description,
        )
        if (image) {
            ContentImage()
        }
        Actions()
    }
}

@Composable
private fun Header(
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        ProfileImage()
        HeaderInfo(
            title = title,
            subtitle = subtitle,
        )
        Spacer(Modifier.weight(1f))
        VUIcon(
            icon = VeganUniverseIcons.MoreOptions,
            contentDescription = "Más opciones",
            onIconClick = {},
        )
    }
}

@Composable
private fun ProfileImage() {
    Image(
        modifier = Modifier
            .padding(top = Spacing_04, start = Spacing_04)
            .size(50.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        painter = painterResource(R.drawable.vegan_restaurant),
        contentDescription = "Imágen del usuario creador del post",
    )
}

@Composable
private fun HeaderInfo(
    title: String,
    subtitle: String,
) {
    Column(modifier = Modifier.padding(start = Spacing_04, top = Spacing_04)) {
        Text(text = title, fontWeight = FontWeight.Medium, maxLines = 2)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
    }
}

@Composable
private fun Description(
    description: String?,
) {
    description?.let {
        Text(
            modifier = Modifier.padding(top = Spacing_04, start = Spacing_04, end = Spacing_07),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ContentImage() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .padding(Spacing_04)
            .clip(ShapeDefaults.Small),
        contentScale = ContentScale.Crop,
        painter = painterResource(R.drawable.vegan_restaurant),
        contentDescription = "Imágen del post"
    )

}

@Composable
private fun Actions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Spacing_02),
    ) {
        VUAssistChip(
            icon = VeganUniverseIcons.Favorite,
            label = "200",
            onClick = {},
            iconDescription = "Me gusta",
            colors = VUAssistChipDefaults.secondaryAssistChipColors(),
        )
        VUAssistChip(
            icon = VeganUniverseIcons.Comment,
            label = "50",
            onClick = {},
            iconDescription = "Comentar",
            colors = VUAssistChipDefaults.secondaryAssistChipColors(),
        )
        VUIcon(
            icon = VeganUniverseIcons.Share,
            contentDescription = "Compartir",
            onIconClick = {},
        )
        Spacer(Modifier.weight(1f))
        VUIcon(
            icon = VeganUniverseIcons.Bookmark,
            contentDescription = "Guardar",
            onIconClick = {},
        )
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