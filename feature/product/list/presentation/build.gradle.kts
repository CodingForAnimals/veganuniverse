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
    implementation(AndroidX.paging)
    implementation(AndroidX.paging_compose)
    implementation(project(":commons:user:domain"))
    implementation(project(Module.Product.List.DOMAIN))
    implementation(project(Module.Product.UI))
}