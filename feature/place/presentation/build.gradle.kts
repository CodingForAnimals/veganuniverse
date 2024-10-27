plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.place.presentation"
}

dependencies {
    implementation(project(":services:location"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-profile:domain"))
    implementation(project(":commons:navigation"))
    implementation(project(":feature:place:domain"))
    implementation(project(":feature:place:shared"))
    implementation(libs.google.mapsLibs)

    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging_compose)
}