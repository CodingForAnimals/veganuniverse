package org.codingforanimals.veganuniverse.featuredtopic.model

import androidx.annotation.DrawableRes
import org.codingforanimals.veganuniverse.model.Post

data class FeaturedTopic(
    @DrawableRes val imageRes: Int,
    val description: String?,
    val posts: List<Post>,
)

private const val TEXTO_PRUEBA =
    "Esta receta es de las mejores que probé, es bastante sencilla, pero requiere de tu tiempo y atención. Ponete una buena canción y hacete un favor con esta receta, creeme que me vas a estar agradeciendo! Para comer vos solo/a o para compartir en familia y amigos!"

private val test_post = Post(
    title = "Receta de tal o cual cosa",
    subtitle = "subtitulo • usuario",
    description = TEXTO_PRUEBA
)

val test_post_list = listOf(
    test_post,
    test_post,
    test_post,
    test_post,
    test_post,
    test_post,
)

val featured_topic = FeaturedTopic(
    imageRes = R.drawable.featured_topic_abc_vegan_test,
    description = "Los temas más relevantes para quienes se inician en el veganismo",
    posts = test_post_list,
)