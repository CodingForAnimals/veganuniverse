plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.ui"

    buildFeatures {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion = versions.composeCompiler
        }
    }
}

dependencies {
    val bom = platform("androidx.compose:compose-bom:${versions.composeBom}")
    add("implementation", bom)
    implementation(libs.compose)
    debugImplementation(libs.composeDebug)
    implementation(Other.IMAGE_CROPPER)
    implementation(Other.SHIMMER)
    implementation(libs.permissionsCompose)
    implementation(project(Module.Core.UI))
}
