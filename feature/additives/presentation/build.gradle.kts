plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.additives.presentation"
}

dependencies {
    implementation(project(":feature:additives:domain"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:navigation"))
}
