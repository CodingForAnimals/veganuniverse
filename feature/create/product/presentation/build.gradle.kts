plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.product.presentation"
}

dependencies {
    implementation(project(Module.Create.Product.DOMAIN))
    implementation(project(Module.Entity.PRODUCT))
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.UI))
    implementation(project(Module.Create.UI))
    implementation(project(Module.Product.UI))
}