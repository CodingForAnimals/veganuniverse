package org.codingforanimals.veganuniverse.product.list.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.utils.TimeAgo

@Composable
fun ProductRowAdditionalInfo(
    additionalInfoState: ProductRowViewModel.ProductAdditionalInfoState,
) {
    Crossfade(
        targetState = additionalInfoState,
        label = "",
        content = { currentState ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(
                        start = Spacing_06,
                        end = Spacing_06,
                        bottom = Spacing_04
                    ),
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                when (currentState) {
                    ProductRowViewModel.ProductAdditionalInfoState.Loading -> {
                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(46.dp)
                                    .fillMaxWidth()
                                    .clip(ShapeDefaults.Small)
                            )
                        }
                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(0.5f)
                                    .clip(ShapeDefaults.Small)
                            )
                        }

                        Box(
                            modifier = Modifier.shimmer(),
                        ) {
                            ShimmerItem(
                                modifier = Modifier
                                    .height(20.dp)
                                    .fillMaxWidth(0.3f)
                                    .clip(ShapeDefaults.Small)
                            )
                        }
                    }

                    is ProductRowViewModel.ProductAdditionalInfoState.Success -> {
                        currentState.additionalInfo.comment?.let { comment ->
                            Text(
                                text = comment,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        currentState.additionalInfo.username?.let { username ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                VUIcon(icon = VUIcons.Profile, contentDescription = "")
                                Text(
                                    modifier = Modifier.wrapContentHeight(),
                                    text = username,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        currentState.additionalInfo.createdAt?.let { createdAt ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing_03),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                VUIcon(icon = VUIcons.Clock, contentDescription = "")
                                Text(
                                    modifier = Modifier.wrapContentHeight(),
                                    text = TimeAgo.getTimeAgo(createdAt),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }

                    ProductRowViewModel.ProductAdditionalInfoState.Idle -> Unit
                }
            }
        }
    )
}