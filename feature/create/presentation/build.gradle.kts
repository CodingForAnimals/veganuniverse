plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.presentation"
}

dependencies {
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons:navigation"))
}
