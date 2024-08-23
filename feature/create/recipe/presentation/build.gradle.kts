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
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-create:presentation"))
    implementation(project(":commons-recipe:shared"))
    implementation(project(":commons:navigation"))
    implementation(project(":commons:network"))
    implementation(project(":commons-recipe:presentation"))
    implementation(project(":feature:create:recipe:domain"))

}