package org.codingforanimals.veganuniverse.community.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.common.R.drawable.featured_topic_abc_vegan_test
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme

@Composable
internal fun FeaturedTopicCard(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    text: String,
    onClick: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .size(125.dp, 190.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(text) },
    ) {
        Image(
            modifier = Modifier.wrapContentHeight(),
            painter = painterResource(id = imageId),
            contentDescription = "ABC del veganismo",
        )
        Text(
            modifier = Modifier.fillMaxSize(),
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun PreviewFeaturedTopicCard() {
    VeganUniverseTheme {
        FeaturedTopicCard(
            imageId = featured_topic_abc_vegan_test,
            text = "Topic",
            onClick = {},
        )
    }
}