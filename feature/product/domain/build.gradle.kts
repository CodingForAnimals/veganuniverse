plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.domain"
}

dependencies {
    implementation(libs.androidx.paging)
    implementation(project(":commons-product:domain"))
    implementation(project(":commons:user:domain"))
}