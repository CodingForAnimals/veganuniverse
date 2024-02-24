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
    implementation(AndroidX.paging)
    implementation(AndroidX.paging_compose)
    implementation(project(":commons:user:domain"))
    implementation(project(":feature:product:domain"))
    implementation(project(Module.Product.UI))
}