plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.analytics"
    compileSdk = 34
    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    bomImplementation(
        libs.firebase.bom,
        listOf(
            libs.firebase.analytics,
            libs.firebase.crashlytics
        )
    )
}
