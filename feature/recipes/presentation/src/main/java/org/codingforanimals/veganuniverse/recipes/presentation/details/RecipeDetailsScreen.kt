@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.core.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemHero
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTags
import org.codingforanimals.veganuniverse.core.ui.shared.FeatureItemTitle
import org.codingforanimals.veganuniverse.core.ui.shared.GenericPost
import org.codingforanimals.veganuniverse.core.ui.shared.HeaderData
import org.codingforanimals.veganuniverse.core.ui.shared.UserInfo
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.presentation.R

@Composable
internal fun RecipeDetailsScreen(
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        VUTopAppBar(
            onBackClick = onBackClick,
            actions = {
                var showMenu by rememberSaveable { mutableStateOf(false) }
                VUIcon(
                    icon = VUIcons.MoreOptions,
                    contentDescription = "",
                    onIconClick = { showMenu = !showMenu },
                )
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Reportar receta") },
                        onClick = {},
                        leadingIcon = {
                            VUIcon(icon = VUIcons.Report, contentDescription = "")
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Sugerir edición") },
                        onClick = {},
                        leadingIcon = {
                            VUIcon(icon = VUIcons.Edit, contentDescription = "")
                        })
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            item {
                FeatureItemHero(
                    imageRes = org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant,
                    icon = VUIcons.RecipesSelected
                )
            }
            item {
                FeatureItemTitle(
                    title = "Mega muzza de papa"
                )
            }
            item { Details() }
            item { UserInfo() }
            item { Description() }
            item { FeatureItemTags(tags) }
            item { Divider() }
            item { Ingredients() }
            item { Divider() }
            item { Steps() }
            item { Comments() }
        }
    }
}

@Composable
private fun Details() {
    Column(modifier = Modifier.padding(horizontal = Spacing_06)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Profile, contentDescription = "")
            Text(text = "4 porciones - 500gr")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
            VUIcon(icon = VUIcons.Clock, contentDescription = "")
            Text(text = "20 minutos")
        }
    }
}

@Composable
private fun Description() {
    Text(
        modifier = Modifier.padding(horizontal = Spacing_06),
        text = description,
    )
}

@Composable
private fun Ingredients() {
    Text(
        text = "Ingredientes",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = Spacing_04, horizontal = Spacing_06)
    )
    ingredients.forEach {
        Row(
            modifier = Modifier
                .padding(horizontal = Spacing_06)
                .padding(bottom = Spacing_04),
        ) {
            VUIcon(icon = VUIcons.Bullet, contentDescription = "")
            Text(modifier = Modifier.padding(start = Spacing_02), text = it)
        }
    }
}

@Composable
private fun Steps() {
    Text(
        text = "Pasos",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = Spacing_04, horizontal = Spacing_06)
    )
    steps.forEachIndexed { index, step ->
        Row(
            modifier = Modifier
                .padding(horizontal = Spacing_06)
                .padding(bottom = Spacing_04),
        ) {
            Badge(
                modifier = Modifier
                    .size(30.dp)
                    .aspectRatio(1f),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                content = {
                    Text(
                        modifier = Modifier.offset(y = (2).dp),
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
            )
            Text(modifier = Modifier.padding(start = Spacing_04), text = step)
        }
    }
}

@Composable
private fun Comments() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Text(
            text = "¿Qué dice la comunidad?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = Spacing_06),
        )

        comments.forEach {
            val header = HeaderData(
                imageRes = R.drawable.test_img_panificados,
                title = {
                    Text(
                        text = it.user,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    val icon = if (it.user == "El Pepe Argento") {
                        VUIcons.Delete
                    } else {
                        VUIcons.Report
                    }
                    VUIcon(icon = icon, contentDescription = "", onIconClick = {})
                }
            )
            GenericPost(
                modifier = Modifier.padding(horizontal = Spacing_06),
                headerData = header,
                content = {
                    Text(text = it.comment)
                },
                actions = {
                    val (favIcon, favColor) = if (it.user == "Pizza Muzza") {
                        Pair(VUIcons.FavoriteFilled, VUAssistChipDefaults.assistChipColors())
                    } else {
                        Pair(VUIcons.Favorite, VUAssistChipDefaults.secondaryAssistChipColors())
                    }
                    VUAssistChip(
                        icon = favIcon,
                        onClick = {},
                        iconDescription = "",
                        label = it.likes.toString(),
                        colors = favColor,
                    )
                    VUAssistChip(
                        icon = VUIcons.Reply,
                        onClick = {},
                        iconDescription = "",
                        label = "Responder",
                        colors = VUAssistChipDefaults.secondaryAssistChipColors(),
                    )
                })
        }
    }
}

private val comments = listOf(
    Comment(
        user = "El Pepe Argento",
        comment = "Hola! Cuánto dura en la heladera?",
        likes = 1,
    ),
    Comment(
        user = "Pizza Muzza",
        comment = "@elpepe dura aprox 1 semana",
        likes = 5,
    ),
    Comment(
        user = "El Pepe Argento",
        comment = "Bárbaro gracias :D",
        likes = 1,
    )
)

data class Comment(
    val user: String,
    val comment: String,
    val likes: Int,
)


@Composable
private fun Divider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
    )
}

private val tags = listOf(
    "Sin tacc",
    "No lácteo",
    "Salado",
    "Picar"
)

private val ingredients = listOf(
    "1 Papa mediana o grande",
    "6 cucharadas de aceite de oliva",
    "1 cucharade de fécula de maíz",
    "1 cucharada de fécula de mandioca",
    "3 cucharadas de levadura nutricional sabor queso",
    "2 cucharadas de jugo de limón",
    "2 cucharaditas de sal",
    "1/2 diente de ajo",
)

private val steps = listOf(
    "Pelamos y cortamos la papa. Ponemos a hervir. Vamos a usar el Agua del hervor, así que no la tires, aproximadamente ½ taza dependiendo del tamaño de tu papa. Deja que la papa se enfríe!",
    "Ponemos en una licuadora o vaso de mixer la Papa y vamos agregando el Agua del hervor hasta que tengamos una consistencia chirle y fluida. Una vez lograda, incorporamos el resto de los ingredientes y procesamos hasta que no queden grumos.",
    "Pasamos toda la mezcla a una cacerola a fuego moderado. Se hace muy rápido, hay que revolver de manera continua con cuchara de madera, unos 3 minutos aproximadamente.",
    "En una pizza queda mejor si lo usas caliente, sino lo colocamos en un molde aceitado para usar cuando necesitemos. ¡A disfrutar!"
)

private val description =
    "Esta receta de queso de papa tipo mozzarella es súper fácil y rica. Queda muy bien en pizzas, canelones, hamburguesas, tostadas o lo que se te ocurra. Lo mejor es que es muy económica de hacer. Es apto celíaco."