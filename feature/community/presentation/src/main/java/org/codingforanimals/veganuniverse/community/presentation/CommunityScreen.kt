package org.codingforanimals.veganuniverse.community.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.delay
import org.codingforanimals.veganuniverse.community.presentation.component.FeaturedTopicCard
import org.codingforanimals.veganuniverse.community.presentation.component.Post
import org.codingforanimals.veganuniverse.core.ui.components.Dropdown
import org.koin.androidx.compose.koinViewModel

@Composable
fun CommunityScreen(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
    viewModel: CommunityScreenViewModel = koinViewModel(),
) {

    var placeholder by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000)
        placeholder = false
    }

    val placeholderModifier = Modifier.placeholder(
        visible = placeholder,
        color = Color.LightGray,
        highlight = PlaceholderHighlight.shimmer(Color.White),
        shape = CircleShape
    )

    FeaturedTopics(topics = featuredTopics, navigateToFeaturedTopic = navigateToFeaturedTopic)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        Text(modifier = placeholderModifier, text = "Community Screen")
        Button(modifier = placeholderModifier, onClick = {}) {
            Text(text = "This is a button")
        }
        TextButton(modifier = placeholderModifier, onClick = {}) {
            Text(text = "Please click me")
        }
    }


//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    when (uiState) {
//        CommunityScreenViewModel.UiState.Loading -> {
//            LoadingScreen()
//        }
//        is CommunityScreenViewModel.UiState.Success -> {
//            CommunityScreen(
//                navigateToFeaturedTopic = navigateToFeaturedTopic,
//                navigateToPost = navigateToPost,
//            )
//        }
//    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.Center),
        )
    }
}

@Composable
private fun CommunityScreen(
    navigateToFeaturedTopic: (String) -> Unit,
    navigateToPost: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                Title("Recomendado por Universo Vegano")
                FeaturedTopics(
                    featuredTopics,
                    navigateToFeaturedTopic,
                )
                Title("Lo que dice nuestra comunidad")
                CommunityFeed(
                    navigateToPost = navigateToPost
                )
            }
        }
    }
}

@Composable
private fun FeaturedTopics(
    topics: List<String>,
    navigateToFeaturedTopic: (String) -> Unit,
) {
    LazyRow(
        modifier = Modifier.wrapContentHeight(),
        contentPadding = PaddingValues(12.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(topics) { topic ->
            FeaturedTopicCard(
                imageId = R.drawable.abc_vegan,
                text = topic,
                onClick = navigateToFeaturedTopic
            )
        }
    }
}

@Composable
private fun CommunityFeed(
    navigateToPost: (String) -> Unit,
) {
    FeedFilters()
    PostList(
        navigateToPost = navigateToPost,
    )
}

@Composable
private fun Title(
    text: String,
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 24.dp, start = 12.dp),
    )
}

@Composable
private fun FeedFilters() {
    Row {
        Dropdown(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
                .background(Color.White),
            items = sortBy,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Dropdown(
            Modifier
                .weight(1f)
                .padding(end = 12.dp)
                .background(Color.White),
            items = filterBy,
        )
    }
}

@Composable
private fun PostList(
    navigateToPost: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        repeat(15) {
            Post(onClick = { navigateToPost("$it") })
        }
    }
}
