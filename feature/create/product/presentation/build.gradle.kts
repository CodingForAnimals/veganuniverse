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
    implementation(project(":feature:create:product:domain"))
    implementation(project(Module.Create.UI))
    implementation(project(Module.Product.UI))
}
