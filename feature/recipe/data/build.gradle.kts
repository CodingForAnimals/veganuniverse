plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipe.data"
}

dependencies {
    implementation(project(":commons:firebase:storage"))
    implementation(libs.androidx.paging)
    implementation(libs.androidx.datastore)

    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.FIRESTORE,
            Firebase.ANALYTICS,
            Firebase.AUTH,
            Firebase.STORAGE,
        )
    )
    implementation(project(":core:auth"))
    implementation(project(":services:firebase"))
}
