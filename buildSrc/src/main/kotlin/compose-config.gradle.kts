import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = versions.composeCompiler
    }
    dependencies {
        add("implementation", project(":core:ui"))
        implementation(libs.compose)
        debugImplementation(libs.composeDebug)
//        val bom = platform("androidx.compose:compose-bom:2023.01.00")
//        add("implementation", bom)
//        add("androidTestImplementation", bom)
    }
}