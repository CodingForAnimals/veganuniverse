plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.validator"
}

dependencies {
    implementation(project(":commons:navigation"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-product:domain"))
    implementation(project(":commons-product:presentation"))
    implementation(project(":commons-recipe:domain"))
    implementation(project(":commons-recipe:presentation"))
    implementation(project(":commons-place:domain"))
    implementation(project(":commons-place:presentation"))
    implementation(project(":feature:registration:presentation"))
    implementation(libs.androidx.paging_compose)
    implementation(libs.androidx.paging)
}
