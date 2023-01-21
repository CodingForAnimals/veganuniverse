package org.codingforanimals.veganuniverse.featuredtopic.presentation.nav

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.featuredtopic.presentation.FeaturedTopicScreen

internal const val topicArg = "topicArg"

fun NavController.navigateToFeaturedTopic(topicName: String) {
    val encodedDest = Uri.encode(topicName)
    this.navigate("topic_route/$encodedDest")
}

fun NavGraphBuilder.featuredTopicGraph() {
    composable(route = "topic_route/{$topicArg}",
        arguments = listOf(
            navArgument(topicArg) { type = NavType.StringType }
        )
    ) {
        val arg = it.arguments?.getString(topicArg)
        FeaturedTopicScreen(arg)
    }
}