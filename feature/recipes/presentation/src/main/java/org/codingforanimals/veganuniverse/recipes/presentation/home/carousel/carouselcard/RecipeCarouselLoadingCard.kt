package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.utils.toDp


@Composable
internal fun RecipeCarouselLoadingCard() {
    val parentView = LocalView.current
    Box(
        modifier = Modifier
            .width((parentView.width / 2.7).toDp())
            .aspectRatio(0.7f)
            .clip(ShapeDefaults.Small)
            .shimmer(),
    ) {
        ShimmerItem(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White)
            ) {
                ShimmerItem(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .padding(
                            top = Spacing_05,
                            start = Spacing_05,
                            end = Spacing_05,
                        )
                        .clip(ShapeDefaults.Small),
                )
                Spacer(modifier = Modifier.height(Spacing_05))
                ShimmerItem(
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(80.dp)
                        .height(30.dp)
                        .padding(
                            end = Spacing_05,
                            bottom = Spacing_05,
                        )
                        .clip(ShapeDefaults.Small),
                )
            }
        }
    }
}