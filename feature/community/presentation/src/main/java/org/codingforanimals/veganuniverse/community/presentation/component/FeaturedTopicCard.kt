package org.codingforanimals.veganuniverse.community.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun FeaturedTopicCard(
    @DrawableRes imageId: Int,
    text: String,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .size(125.dp, 190.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick(text) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
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