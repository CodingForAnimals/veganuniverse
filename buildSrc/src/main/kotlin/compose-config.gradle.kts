import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = versions.composeCompiler
    }
    dependencies {
        add("implementation", project(Module.Core.UI))
        add("implementation", project(Module.UI))
        val bom = platform("androidx.compose:compose-bom:${versions.composeBom}")
        add("implementation", bom)
        implementation(libs.compose)
        debugImplementation(libs.composeDebug)

        implementation(libs.placeholder)

        implementation(Koin.libs)
//        add("androidTestImplementation", bom)
    }
}