plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.data"
}

dependencies {
    implementation(libs.androidx.paging)
}
