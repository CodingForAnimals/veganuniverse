plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.profile.data"
}

dependencies {
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.database,
        )
    )
    implementation(libs.androidx.room)
    implementation(libs.androidx.roomKtx)
    ksp(libs.androidx.roomRuntimeCompiler)
}
