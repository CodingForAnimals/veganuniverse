plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.model"
}

dependencies {
    implementation(project(":feature:onboarding:data"))

}