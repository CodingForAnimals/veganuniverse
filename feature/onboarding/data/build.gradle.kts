plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.data"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(libs.koinAndroid)
}