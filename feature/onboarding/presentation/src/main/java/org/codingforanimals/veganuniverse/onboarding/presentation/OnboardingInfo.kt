package org.codingforanimals.veganuniverse.onboarding.presentation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class OnboardingInfo(
    val title: String,
    val subtitle: String,
    val imageId: Int,
    val imageSize: Dp,
)

internal val onboardingInfo = listOf(
    OnboardingInfo(title = "Te damos la bienvenida a\nUniverso Vegano", subtitle = "Una espacio donde encontrás toda la información que necesitás.", imageId = org.codingforanimals.veganuniverse.commons.ui.R.drawable.logo, imageSize = 150.dp),
    OnboardingInfo(title = "Para veganos y no veganos", subtitle = "Tendemos puentes para que ser vegan sea cada día un poco más fácil y accesible", imageId = R.drawable.onboarding_image_1, imageSize = 300.dp),
    OnboardingInfo(title = "Todo en una sola app", subtitle = "Buscá productos aptos\nDescubrí lugares para comer\nAprendé y compartí recetas\ny mucho más que está por venir...", imageId = R.drawable.onboarding_image_2, imageSize = 300.dp),
)
