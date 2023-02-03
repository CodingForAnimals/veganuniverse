@file:OptIn(ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.onboarding.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingViewModel.Action.OnUserDismissOnboardingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    onDismiss: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    HandleSideEffects(
        effectsFlow = viewModel.sideEffect,
        onDismiss = onDismiss,
    )

    OnboardingScreen(
        onAction = viewModel::onAction,
    )
}

@Composable
private fun OnboardingScreen(
    onAction: (OnboardingViewModel.Action) -> Unit,
) {
    BackgroundImage()
    Box {
        TextBackground()

        val pagerState = rememberPagerState()
        ScrollableContent(onboardingInfo, pagerState)
        NavigationButtons(
            info = onboardingInfo,
            pagerState = pagerState,
            onDismiss = { onAction(OnUserDismissOnboardingScreen) })
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.onboarding_background),
        contentScale = ContentScale.FillBounds,
        contentDescription = "onboarding background"
    )
}

@Composable
private fun TextBackground() {
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))
                .background(Color.White),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        }
    }
}

@Composable
private fun ScrollableContent(
    info: List<OnboardingInfo>,
    pagerState: PagerState,
) {
    HorizontalPager(pageCount = info.size, state = pagerState) { page ->
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(info[page].imageSize)
                        .align(Alignment.Center),
                    model = info[page].imageId,
                    contentDescription = "Onboarding descriptive image",
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = onboardingInfo[page].title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = onboardingInfo[page].subtitle,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BoxScope.NavigationButtons(
    info: List<OnboardingInfo>,
    pagerState: PagerState,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.align(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OnboardingIndicators(count = info.size, index = pagerState.currentPage)

        val isLastPage = pagerState.currentPage == info.size - 1

        Crossfade(targetState = isLastPage) {
            if (it) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentSize()
                            .weight(1f),
                        content = { Text(text = "¡Comenzar!") },
                        onClick = onDismiss,
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                ) {
                    TextButton(
                        modifier = Modifier
                            .wrapContentSize()
                            .weight(1f),
                        onClick = onDismiss,
                        content = { Text(text = "Saltar") },
                    )
                    Button(
                        modifier = Modifier
                            .wrapContentSize()
                            .weight(1f),
                        content = { Text(text = "Siguiente") },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun OnboardingIndicators(count: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = { repeat(count) { Indicator(it == index) } },
    )
}

@Composable
private fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun HandleSideEffects(
    effectsFlow: Flow<OnboardingViewModel.SideEffect>,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(Unit) {
        effectsFlow.onEach { effect ->
            when (effect) {
                OnboardingViewModel.SideEffect.DismissOnboardingScreen -> onDismiss()
            }
        }.collect()
    }
}