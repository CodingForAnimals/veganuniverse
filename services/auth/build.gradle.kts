plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.services.auth"
}

dependencies {
    bomImplementation(
        Firebase.BOM,
        listOf(Firebase.AUTH, Firebase.STORAGE, Firebase.REALTIME_DATABASE, Firebase.FIRESTORE)
    )
    implementation(Google.AUTH)
}