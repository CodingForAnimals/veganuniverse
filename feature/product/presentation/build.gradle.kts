plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.presentation"
}

dependencies {
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging_compose)
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:navigation"))
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-product:shared"))
    implementation(project(":commons-product:presentation"))
    implementation(project(":feature:product:domain"))
}
