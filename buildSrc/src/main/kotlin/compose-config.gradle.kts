import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = versions.composeCompiler
    }
    dependencies {
        add("implementation", project(":core:ui"))
        val bom = platform("androidx.compose:compose-bom:${versions.composeBom}")
        add("implementation", bom)
        implementation(libs.material3)
        implementation(libs.compose)
        debugImplementation(libs.composeDebug)

        implementation(libs.placeholder)

        implementation(libs.koinAndroid)
        implementation(libs.koinAndroidCompose)
//        add("androidTestImplementation", bom)
    }
}