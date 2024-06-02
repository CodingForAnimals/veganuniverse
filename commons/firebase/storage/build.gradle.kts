plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.firebase.storage"
}

dependencies {
    bomImplementation(
        libs.firebase.bom,
        libs.firebase.storage,
    )
}
