package org.codingforanimals.veganuniverse.common

private const val TEXTO_PRUEBA =
    "Esta receta es de las mejores que probé, es bastante sencilla, pero requiere de tu tiempo y atención. Ponete una buena canción y hacete un favor con esta receta, creeme que me vas a estar agradeciendo! Para comer vos solo/a o para compartir en familia y amigos!"

private val test_post = Post(
    title = "Receta de tal o cual cosa",
    subtitle = "subtitulo • usuario",
    description = TEXTO_PRUEBA,
    image = null,
)
private val test_post_with_image = Post(
    title = "Receta de tal o cual cosa",
    subtitle = "subtitulo • usuario",
    description = TEXTO_PRUEBA,
    image = "",
)

val test_post_list = listOf(
    test_post,
    test_post,
    test_post_with_image,
    test_post,
    test_post_with_image,
    test_post,
)
