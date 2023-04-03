@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R.drawable.vegan_restaurant
import org.codingforanimals.veganuniverse.core.ui.community.Post
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUTag
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.DarkPurple
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_01
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.presentation.R

@Composable
internal fun RecipeDetailsScreen(
    onBackClick: () -> Unit,
) {
    BackHandler(onBack = onBackClick)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            item { Hero() }
            item { Title() }
            item { Details() }
            item { UserInfo() }
            item { Description() }
            item { Tags() }
            item { Divider() }
            item { Ingredients() }
            item { Divider() }
            item { Steps() }
            item { Comments() }
        }
        TopAppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
            navigationIcon = {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = VUIcons.Close.imageVector,
                        contentDescription = "Atrás",
                    )
                }
            },
            title = {},
        )
    }
}

@Composable
private fun Hero() {
    Box(Modifier.padding(bottom = Spacing_02)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.test_img_facturas),
            contentDescription = "",
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(Spacing_02)
                .background(DarkPurple),
        )
        Card(
            modifier = Modifier
                .offset(y = (20).dp)
                .align(Alignment.BottomEnd)
                .wrapContentSize()
                .padding(end = Spacing_06)
                .clip(CircleShape)
                .border(Spacing_01, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .align(Alignment.CenterEnd),
            colors = CardDefaults.cardColors(
                containerColor = DarkPurple,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            VUIcon(
                modifier = Modifier.padding(Spacing_04),
                icon = VUIcons.RecipesSelected,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun Title() {
    Row(
        modifier = Modifier.padding(horizontal = Spacing_06),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Mega muzza de papa",
            style = MaterialTheme.typography.titleLarge
        )
        Row {
            VUIcon(icon = VUIcons.Share, contentDescription = "", onIconClick = {})
            VUIcon(icon = VUIcons.Bookmark, contentDescription = "", onIconClick = {})
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
private fun UserInfo() {
    Row(
        modifier = Modifier.padding(horizontal = Spacing_06),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(vegan_restaurant),
            contentDescription = "Imágen del usuario creador del post",
        )
        Column {
            Text(text = "@PizzaMuzza", fontWeight = FontWeight.SemiBold)
            Text(text = "Pablo Rago")
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
private fun Tags() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(unbounded = true)
            .padding(horizontal = Spacing_06),
        horizontalArrangement = Arrangement.spacedBy(Spacing_06),
    ) {
        items(items = tags, itemContent = {
            VUTag(label = it)
        })
    }
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
    val comments = listOf(
        Pair("Nacho", "Hola! Cuánto dura en la heladera?"),
        Pair("Pizza Muzza", "Una semana aproximadamente"),
    )
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
            Post(
                modifier = Modifier.padding(horizontal = Spacing_06),
                title = it.first,
                subtitle = it.second
            )
        }
    }
}

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