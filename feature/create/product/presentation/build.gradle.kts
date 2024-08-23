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
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-create:presentation"))
    implementation(project(":commons-product:domain"))
    implementation(project(":commons:navigation"))
    implementation(project(":commons-product:presentation"))
    implementation(project(":feature:create:product:domain"))
}
