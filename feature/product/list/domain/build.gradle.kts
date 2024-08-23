plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.list.domain"
}

dependencies {
    implementation(project(Module.Core.AUTH))
}