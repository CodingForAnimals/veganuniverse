package org.codingforanimals.veganuniverse.shared.ui.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import org.codingforanimals.veganuniverse.core.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.core.ui.animation.shimmer
import org.codingforanimals.veganuniverse.core.ui.components.VUImage
import org.codingforanimals.veganuniverse.core.ui.icons.VUImages
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme

@Composable
fun SimpleCard(
    modifier: Modifier = Modifier,
    model: SimpleCardItem,
    onClick: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable(onClick = { onClick(model.id) })
            .shadow(elevation = 10.dp, shape = ShapeDefaults.Small),
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.weight(1f),
            model = model.imageRef,
            contentScale = ContentScale.Crop,
            contentDescription = model.title,
            loading = {
                ShimmerItem(modifier = Modifier.fillMaxSize())
            },
            error = {
                Box(modifier = Modifier.fillMaxSize()) {
                    VUImage(
                        modifier = Modifier
                            .padding(top = Spacing_06)
                            .align(Alignment.Center),
                        image = VUImages.ErrorCat,
                    )
                }
            }
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .heightIn(min = 50.dp)
                .fillMaxWidth()
                .padding(vertical = Spacing_04, horizontal = Spacing_02),
            text = model.title,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun LoadingSimpleCard(modifier: Modifier = Modifier) {
    ShimmerItem(
        modifier = modifier
            .clip(CardDefaults.shape)
            .shimmer()
    )
}

@Composable
@Preview
private fun PreviewSimpleCard() {
    VeganUniverseTheme {
        Surface {
            Column(
                modifier = Modifier.padding(Spacing_06),
                verticalArrangement = Arrangement.spacedBy(Spacing_06)
            ) {
                SimpleCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                    model = SimpleCardItem("id", "Item title", "image ref"),
                    onClick = {},
                )
                Spacer(modifier = Modifier.height(50.dp))
                LoadingSimpleCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                )
            }
        }
    }
}