package org.codingforanimals.veganuniverse.featuredtopic.presentation.nav

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.featuredtopic.presentation.FeaturedTopicScreen

object FeaturedTopicDestination : Destination(route = "topic_route") {
    const val topicArgument = "topic_argument"
}

fun NavController.navigateToFeaturedTopic(topicName: String) {
    val encodedDest = Uri.encode(topicName)
    this.navigate("${FeaturedTopicDestination.route}/$encodedDest")
}

fun NavGraphBuilder.featuredTopicGraph(
    onBackClick: () -> Unit,
) {
    composable(route = "${FeaturedTopicDestination.route}/{${FeaturedTopicDestination.topicArgument}}",
        arguments = listOf(
            navArgument(FeaturedTopicDestination.topicArgument) { type = NavType.StringType }
        )
    ) {
        val arg = it.arguments?.getString(FeaturedTopicDestination.topicArgument)
        FeaturedTopicScreen(
            onBackClick = onBackClick
        )
    }
}