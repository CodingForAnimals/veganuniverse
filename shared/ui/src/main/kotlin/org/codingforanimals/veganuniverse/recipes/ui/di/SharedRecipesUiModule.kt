package org.codingforanimals.veganuniverse.recipes.ui.di

import org.koin.dsl.module

val sharedRecipesUiModule = module {
    // me parece que esto no lo puedo modularizar porque
    // estoy usando una tag como parametro
    // y dentro del perfil necesito pasar directamente las 3 recetas
    // como deferreds... tengo que sentarme bien..
}