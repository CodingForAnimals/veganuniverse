plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.model"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":feature:onboarding:data"))
    implementation(libs.koinAndroid)
}