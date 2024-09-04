plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.thank_you.presentation"
}

dependencies {
    implementation(project(":commons-create:domain"))
}
