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
    implementation(project(":commons:recipe:shared"))
    implementation(project(":commons:recipe:presentation"))
    implementation(project(":core:common"))
    implementation(project(":core:auth"))
    implementation(project(":feature:create:recipe:domain"))
    implementation(project(":ui:recipes"))
    implementation(project(":ui"))
    implementation(project(":entity:recipes"))
    implementation(project(":ui:create"))

}