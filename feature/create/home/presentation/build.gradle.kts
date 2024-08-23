plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.home.presentation"
}

dependencies {
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:navigation"))
}