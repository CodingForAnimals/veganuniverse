plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.app"

    defaultConfig {
        applicationId = "org.codingforanimals.veganuniverse"
        versionCode = 2
        versionName = "1.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\agusm\\Dev\\codingforanimals\\veganuniverse\\vukey.jks")
            storePassword = "123123"
            keyAlias = "vukey"
            keyPassword = "123123"
        }
    }

    buildTypes {
        named("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation(project(":feature:onboarding:presentation"))
    implementation(project(":feature:profile:presentation"))
    implementation(project(":feature:community:presentation"))
    implementation(project(":feature:featuredtopic:presentation"))
    implementation(project(":feature:recipes:presentation"))
    implementation(project(":feature:post:presentation"))
    implementation(project(":feature:places:presentation"))
    implementation(project(":feature:registration:presentation"))
    implementation(project(":feature:notifications:presentation"))
    implementation(project(":feature:create:presentation"))
    implementation(project(":feature:search:presentation"))
    implementation(project(":feature:settings:presentation"))
    implementation(project(":core:common"))
    implementation(project(":core:location"))
    implementation("com.google.maps.android:maps-compose:${versions.googleMapCompose}")
    implementation("com.google.android.gms:play-services-maps:${versions.googleMapServices}")

    implementation("androidx.core:core-splashscreen:${versions.splashScreen}")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // For TimeManager
//    implementation("com.google.android.things:androidthings:1.0")

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies


//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
}