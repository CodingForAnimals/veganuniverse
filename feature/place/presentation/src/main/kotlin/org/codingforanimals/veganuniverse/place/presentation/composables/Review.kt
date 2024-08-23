package org.codingforanimals.veganuniverse.place.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.components.RatingBar
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

@Composable
internal fun Review(
    modifier: Modifier = Modifier,
    username: String?,
    rating: Int,
    title: String?,
    description: String?,
    date: String?,
    borderStroke: BorderStroke? = null,
    actionIcon: Int,
    onActionIconClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        border = borderStroke,
    ) {
        Row(
            modifier = Modifier.padding(start = Spacing_04, bottom = Spacing_04, top = Spacing_03),
            horizontalArrangement = Arrangement.spacedBy(Spacing_04)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing_02)
            ) {
                username?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                RatingBar(rating = rating)

            }
            IconButton(onClick = onActionIconClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(actionIcon),
                    contentDescription = null,
                )
            }
        }
        title?.let {
            Text(
                modifier = Modifier.padding(start = Spacing_04, end = Spacing_04),
                text = it,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        description?.let {
            Text(
                modifier = Modifier.padding(
                    start = Spacing_04,
                    end = Spacing_04,
                    bottom = Spacing_04
                ),
                text = it
            )
        }
        date?.let {
            Text(
                modifier = Modifier.padding(
                    start = Spacing_04,
                    end = Spacing_04,
                    bottom = Spacing_04
                ),
                text = it
            )
        }
    }

//    Spacer(modifier = Modifier.heightIn(50.dp))
//    val header = HeaderData(
//        imageRes = R.drawable.vegan_restaurant,
//        title = {
//            Column {
//                username?.let {
//                    Text(
//                        text = it,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.SemiBold,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                    )
//                }
//                RatingBar(rating = rating)
//            }
//        },
//        actions = {
//            VUIcon(
//                icon = actionIcon,
//                onIconClick = onActionIconClick,
//            )
//        }
//    )
//    GenericPost(
//        modifier = modifier,
//        headerData = header,
//        border = borderStroke,
//        content = {
//            title?.let { Text(text = it, fontWeight = FontWeight.SemiBold) }
//            description?.let {
//                Text(text = description, style = MaterialTheme.typography.bodyMedium)
//            }
//            date?.let { Text(text = it) }
//        },
//    )
}

@Preview
@Composable
fun PreviewReview() {
    Review(
        username = "User name",
        rating = 4,
        title = "Review title",
        description = "This is the review description. Can be either 1 line or multiple lines long.",
        date = "Date",
        actionIcon = VUIcons.Report.id,
        onActionIconClick = {},
    )
}
