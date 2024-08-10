plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.services.google.places"
}

dependencies {
    implementation(project(":commons-place:shared"))
    api(libs.google.places)
}