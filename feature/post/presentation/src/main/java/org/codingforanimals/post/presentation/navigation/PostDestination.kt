package org.codingforanimals.post.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.post.presentation.PostScreen
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object PostDestination : Destination(route = "post_route") {
    const val postIdArgument = "post_id_arg"
}

fun NavController.navigateToPost(postId: String) {
    val encodedId = Uri.encode(postId)
    navigate("${PostDestination.route}/$encodedId")
}

fun NavGraphBuilder.postGraph() {
    composable(
        route = "${PostDestination.route}/{${PostDestination.postIdArgument}}",
        arguments = listOf(
            navArgument(PostDestination.postIdArgument) { type = NavType.StringType }
        ),
        content = {
            val arg = it.arguments?.getString(PostDestination.postIdArgument)
            PostScreen(arg)
        }
    )
}