package org.codingforanimals.post.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.post.presentation.PostScreen

private const val postIdArg = "post_id_arg"
private const val postNavigationRoute = "post_route"

fun NavController.navigateToPost(postId: String) {
    val encodedId = Uri.encode(postId)
    navigate("$postNavigationRoute/$encodedId")
}

fun NavGraphBuilder.postGraph() {
    composable(
        route = "$postNavigationRoute/{$postIdArg}",
        arguments = listOf(
            navArgument(postIdArg) { type = NavType.StringType }
        ),
        content = {
            val arg = it.arguments?.getString(postIdArg)
            PostScreen(arg)
        }
    )
}