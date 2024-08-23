plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.domain"
}

dependencies {
    implementation(AndroidX.paging)
    implementation(project(":feature:product:data"))
    implementation(project(Module.Core.AUTH))
}