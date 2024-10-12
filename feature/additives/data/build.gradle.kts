plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.additives.data"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.firebase.database)
    implementation(libs.androidx.room)
    implementation(libs.androidx.roomKtx)
    ksp(libs.androidx.roomRuntimeCompiler)
}
