plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.shared.places.ui"

}

dependencies {
    implementation(project(Module.Entity.RECIPES))
    api(project(Module.Places.MODEL))
    implementation(project(Module.Core.COMMON))
}