plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.list.domain"
}

dependencies {
    implementation(AndroidX.paging)
    implementation(project(Module.Product.List.DATA))
    implementation(project(Module.Core.AUTH))
}