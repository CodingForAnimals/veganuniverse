plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.recipe.presentation"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Create.Recipe.DOMAIN))
    implementation(project(Module.Recipes.UI))
    implementation(project(Module.UI))
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Create.UI))

}