plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.user.domain"
}

dependencies {
    implementation(project(":commons:user:data"))

    bomImplementation(libs.firebase.bom, listOf(libs.firebase.auth))
}
