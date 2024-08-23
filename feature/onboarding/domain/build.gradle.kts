plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.domain"
}

dependencies {
    implementation(project(":feature:onboarding:data"))

}