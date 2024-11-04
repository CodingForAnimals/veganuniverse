plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.app"

    defaultConfig {
        applicationId = "org.codingforanimals.veganuniverse"
        versionCode = 24
        versionName = "1.3.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("/Users/agustin.magne/agus/veganuniverse/veganuniverse-keystore-release.jks")
//            storeFile =
//                file("C:\\Users\\agusm\\Dev\\codingforanimals\\veganuniverse\\veganuniverse-keystore-release.jks")
            storePassword = "veganuniverse1324"
            keyAlias = "key0"
            keyPassword = "veganuniverse1324"
        }
    }

    buildTypes {
        release {
            manifestPlaceholders["appLinksHost"] = "veganuniverse-prod.firebaseapp.com"
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            manifestPlaceholders["appLinksHost"] = "veganuniverse-a924e.firebaseapp.com"
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    /**
     * TODO remvove depencendy once okhttp3 is automatically updated to 5.0.0 in external dependencies
     */
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    implementation(project(":feature:onboarding:presentation"))
    implementation(project(":feature:onboarding:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(project(":commons:user:domain"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:create:presentation"))
    implementation(project(":feature:product:presentation"))
    implementation(project(":feature:recipe:presentation"))
    implementation(project(":feature:place:presentation"))
    implementation(project(":feature:registration:presentation"))
    implementation(project(":services:location"))
    implementation(project(":commons:navigation"))
    implementation(project(":feature:additives:presentation"))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.google.gson)

    implementation("androidx.core:core-splashscreen:${versions.splashScreen}")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}