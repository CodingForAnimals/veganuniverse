@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.ui.shared

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

data class HeaderData(
    @DrawableRes val imageRes: Int,
    val title: @Composable () -> Unit,
    val actions: @Composable RowScope.() -> Unit,
)

@Composable
fun GenericPost(
    modifier: Modifier = Modifier,
    headerData: HeaderData,
    content: @Composable () -> Unit,
    actions: @Composable () -> Unit = {},
    border: BorderStroke? = null,
    elevation: CardElevation? = null,
) {
    Card(
        modifier = modifier,
        border = border,
        elevation = elevation ?: CardDefaults.cardElevation(),
    ) {
        Header(headerData)
        Column(modifier = Modifier.padding(Spacing_04)) {
            content()
        }
//        Row(modifier = Modifier.padding(horizontal = Spacing_02)) {
        actions()
//        }
    }
}

@Composable
private fun Header(
    headerData: HeaderData,
) {
    with(headerData) {
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
                painter = painterResource(imageRes),
                contentDescription = "Imágen",
            )
            Column(
                modifier = Modifier
                    .padding(top = Spacing_04, start = Spacing_04)
                    .heightIn(max = 50.dp)
            ) {
                title()
            }
            Spacer(Modifier.weight(1f))
            actions()
        }
    }
}

@Preview
@Composable
private fun PreviewGenericPost() {
    VeganUniverseTheme {
        GenericPost(
            headerData = HeaderData(
                imageRes = R.drawable.vegan_restaurant,
                title = { Text(text = "Title") },
                actions = {
                    VUIcon(
                        icon = VUIcons.MoreOptions,
                        onIconClick = {},
                        contentDescription = "",
                    )
                },
            ),
            content = { Text(text = "content") })
    }
}