package org.codingforanimals.veganuniverse.community.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codingforanimals.veganuniverse.community.presentation.R


@Composable
internal fun Post(
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Image(
                    modifier = Modifier.size(38.dp),
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile picture",
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = "Dulce de almendras in-cre-í-ble!!",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Recetas • Dani.ella",
                        fontSize = 15.sp,
                    )
                }
            }
            Icon(
                modifier = Modifier.align(Alignment.Top),
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = "Profile picture"
            )
        }

        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            fontSize = 15.sp,
            text = "Esta receta es de las mejores que probé, es bastante sencilla, pero requiere de tu tiempo y atención. Ponete una buena canción y ... más"
        )


        Row(
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        ) {
            var isLiked by remember { mutableStateOf(false) }
            Crossfade(targetState = isLiked) {
                val (imageVector, tint) = if (it) {
                    Pair(Icons.Rounded.Favorite, Color.Red)
                } else {
                    Pair(Icons.Rounded.FavoriteBorder, Color.Black)
                }
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { isLiked = !isLiked },
                    contentDescription = "Like button",
                    imageVector = imageVector,
                    tint = tint,
                )
            }

            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "100", fontSize = 15.sp
            )
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Outlined.ModeComment, contentDescription = "Comment button",
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "20", fontSize = 15.sp
            )
            Icon(
                imageVector = Icons.Outlined.Share, contentDescription = "Share button",
            )
        }
    }
}