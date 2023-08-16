plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.model"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(":feature:onboarding:data"))
}