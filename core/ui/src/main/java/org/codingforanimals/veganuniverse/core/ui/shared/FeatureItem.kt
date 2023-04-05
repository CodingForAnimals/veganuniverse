@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.core.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTag
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.DarkPurple
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_01
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06


@Composable
fun FeatureItemHero(
    @DrawableRes imageRes: Int,
    icon: Icon,
) {
    Box(Modifier.padding(bottom = Spacing_02)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            contentScale = ContentScale.Crop,
            painter = painterResource(imageRes),
            contentDescription = "",
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(Spacing_02)
                .background(DarkPurple),
        )
        Card(
            modifier = Modifier
                .offset(y = (20).dp)
                .align(Alignment.BottomEnd)
                .wrapContentSize()
                .padding(end = Spacing_06)
                .clip(CircleShape)
                .border(Spacing_01, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .align(Alignment.CenterEnd),
            colors = CardDefaults.cardColors(
                containerColor = DarkPurple,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            VUIcon(
                modifier = Modifier.padding(Spacing_04),
                icon = icon,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun FeatureItemTitle(
    title: String,
    subtitle: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(start = Spacing_06, end = Spacing_06, bottom = Spacing_04),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            subtitle()
        }
        Row {
            VUIcon(icon = VUIcons.Share, contentDescription = "", onIconClick = {})
            VUIcon(icon = VUIcons.Bookmark, contentDescription = "", onIconClick = {})
        }
    }
}

@Composable
fun UserInfo() {
    Row(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.vegan_restaurant),
            contentDescription = "Imágen del usuario creador del post",
        )
        Column {
            Text(text = "@PizzaMuzza", fontWeight = FontWeight.SemiBold)
            Text(text = "Pablo Rago")
        }
    }
}

@Composable
fun FeatureItemTags(
    tags: List<String>
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .padding(horizontal = Spacing_06),
        horizontalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        tags.forEach { VUTag(label = it) }
    }
}

@Composable
fun FeatureItemComments() {
    val comments = listOf(
        Pair("Nacho", "Hola! Cuánto dura en la heladera?"),
        Pair("Pizza Muzza", "Una semana aproximadamente"),
    )
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Text(
            text = "¿Qué dice la comunidad?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing_06),
        )
        comments.forEach {
            Post(
                modifier = Modifier.padding(horizontal = Spacing_06),
                title = it.first,
                subtitle = it.second
            )
        }
    }
}