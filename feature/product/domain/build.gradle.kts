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
    implementation(project(":commons:data"))
    implementation(project(":services:firebase-storage"))
    implementation(project(":feature:product:data"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-profile:domain"))
}
