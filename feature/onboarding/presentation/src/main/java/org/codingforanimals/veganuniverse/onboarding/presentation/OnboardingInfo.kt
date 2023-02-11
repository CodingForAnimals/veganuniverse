package org.codingforanimals.veganuniverse.onboarding.presentation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R.drawable.logo

data class OnboardingInfo(
    val title: String,
    val subtitle: String,
    val imageId: Int,
    val imageSize: Dp,
)

internal val onboardingInfo = listOf(
    OnboardingInfo(title = "Te damos la bienvenida a\nUniverso Vegano", subtitle = "Un espacio donde encontrar toda la información que necesitás", imageId = logo, imageSize = 150.dp),
    OnboardingInfo(title = "Una red vegana", subtitle = "Tendemos puentes para que ser vegan sea cada día un poco más fácil y accesible", imageId = R.drawable.onboarding_image_1, imageSize = 300.dp),
    OnboardingInfo(title = "Información oportuna", subtitle = "Recetas\nLugares\nProductos aptos\nPsicología vegana\ny mucho más...", imageId = R.drawable.onboarding_image_2, imageSize = 300.dp),
)