plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.storage"
}

dependencies {
    implementation(project(":entity:recipes"))
    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.FIRESTORE
        )
    )
}