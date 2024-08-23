plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.recipe.presentation"
}

dependencies {
    implementation(project(":commons-recipe:shared"))
}
