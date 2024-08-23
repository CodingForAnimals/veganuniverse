plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.user.data"
}

dependencies {
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.firestore,
            libs.firebase.auth,
        )
    )
    implementation(libs.androidx.datastore)
}