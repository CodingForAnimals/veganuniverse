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
    implementation(project(":commons:product:domain"))
    implementation(project(":commons:product:presentation"))
    implementation(project(":feature:create:product:domain"))
    implementation(project(":ui:create"))
    implementation(project(":ui:product"))
}
