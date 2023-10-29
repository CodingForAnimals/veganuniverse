plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.shared.ui"

}

dependencies {
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Core.COMMON))
}