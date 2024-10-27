plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipe.data"
}

dependencies {
    implementation(project(":feature:recipe:shared"))
    implementation(project(":commons:data"))
    implementation(project(":services:firebase-storage"))
    implementation(libs.androidx.paging)
    implementation(libs.androidx.datastore)

    bomImplementation(
        libs.firebase.bom,
        listOf(
            libs.firebase.firestore,
            libs.firebase.database,
            libs.firebase.analytics,
            libs.firebase.auth,
            libs.firebase.storage
        )
    )
}
