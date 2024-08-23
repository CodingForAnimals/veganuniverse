plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipe.data"
}

dependencies {
    implementation(project(":services:firebase-storage"))
    implementation(libs.androidx.paging)
    implementation(libs.androidx.datastore)

    bomImplementation(
        libs.firebase.bom,
        listOf(
            libs.firebase.firestore,
            libs.firebase.analytics,
            libs.firebase.auth,
            libs.firebase.storage
        )
    )
}
