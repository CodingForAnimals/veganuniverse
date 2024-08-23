import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configure<BaseExtension> {
    compileSdkVersion(34)
//    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdk = 21
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    plugins {
        id("kotlin-android")
        id("com.google.firebase.crashlytics")
    }

    dependencies {
        implementation(Koin.koinAndroid)
        add("implementation", project(":commons:analytics"))
    }
}