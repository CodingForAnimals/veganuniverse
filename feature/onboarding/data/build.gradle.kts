plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.data"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
}