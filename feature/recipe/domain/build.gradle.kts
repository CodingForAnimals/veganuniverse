plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.domain"
}

dependencies {
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-recipe:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(libs.androidx.paging)
}