plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.community.presentation"
}

dependencies {
    implementation(project(Module.Core.COMMON))
}