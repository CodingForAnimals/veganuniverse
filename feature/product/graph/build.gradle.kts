plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.graph"
}

dependencies {
    implementation(project(Module.Product.Categories.PRESENTATION))
    implementation(project(Module.Product.List.PRESENTATION))
}