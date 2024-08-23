plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.list.presentation"
}

dependencies {
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Product.UI))
}