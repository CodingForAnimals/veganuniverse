plugins {
    id("com.android.application")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.app"

    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\agusm\\Dev\\codingforanimals\\vegauniversekey.jks")
            storePassword = "123123"
            keyAlias = "Vegan Universe"
            keyPassword = "123123"
        }
    }

    defaultConfig {
        applicationId = "org.codingforanimals.veganuniverse"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(project(":feature:onboarding:presentation"))
    implementation(project(":feature:community:presentation"))
    implementation(project(":feature:featuredtopic:presentation"))
    implementation(project(":feature:recipes:presentation"))
    implementation(project(":feature:post:presentation"))
    implementation(project(":feature:map:presentation"))
    implementation(project(":feature:site:presentation"))
    implementation(project(":feature:places:presentation"))
    implementation(project(":feature:registration:presentation"))
    implementation(project(":core:common"))
    implementation("com.google.maps.android:maps-compose:${versions.googleMapCompose}")
    implementation("com.google.android.gms:play-services-maps:${versions.googleMapServices}")

    implementation("androidx.core:core-splashscreen:${versions.splashScreen}")

    implementation(libs.koinAndroid)


//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
//    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
}