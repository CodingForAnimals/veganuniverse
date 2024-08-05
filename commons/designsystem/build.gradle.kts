plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.designsystem"

    buildFeatures {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion = versions.composeCompiler
        }
    }
}


dependencies {
    val bom = platform("androidx.compose:compose-bom:${libs.androidx.composeBom}")
    add("implementation", bom)
    implementation(libs.androidx.compose)
    debugImplementation(libs.composeDebug)
}