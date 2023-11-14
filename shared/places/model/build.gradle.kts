plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.shared.places.model"
}

dependencies {
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Core.COMMON))
}