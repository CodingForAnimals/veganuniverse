plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.recipe.domain"
}

dependencies {
    implementation(project(":commons-recipe:data"))
    api(project(":commons-recipe:shared"))
    implementation(libs.androidx.paging)
}
