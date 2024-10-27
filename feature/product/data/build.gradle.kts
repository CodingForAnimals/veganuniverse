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
    implementation(project(":services:firebase-storage"))
    implementation(libs.androidx.datastore)
    implementation(libs.firebase.database)
    implementation(libs.androidx.room)
    implementation(libs.androidx.roomKtx)
    ksp(libs.androidx.roomRuntimeCompiler)
}
