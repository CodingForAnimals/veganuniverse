plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.network"
}

dependencies {
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(libs.firebase.firestore),
    )
}
