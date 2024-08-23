plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.data"
}

dependencies {
    implementation(project(":core:common"))
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
