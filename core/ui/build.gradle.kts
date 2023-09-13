plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.core.ui"

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

    implementation(libs.permissionsCompose)

    implementation(Google.PLACEHOLDER)
    implementation(Other.IMAGE_CROPPER)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}