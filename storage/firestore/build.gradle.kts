plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.firestore.storage"
}

dependencies {
    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.FIRESTORE
        )
    )
}