import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = versions.composeCompiler
    }
    dependencies {
        add("implementation", project(":commons:ui"))
        add("implementation", project(":commons:designsystem"))
        val bom = platform("androidx.compose:compose-bom:${libs.androidx.composeBom}")
        add("implementation", bom)
        debugImplementation(libs.composeDebug)
        implementation(libs.androidx.compose)
        implementation(libs.coil)

        implementation(libs.placeholder)

        implementation(Koin.libs)
//        add("androidTestImplementation", bom)
    }
}