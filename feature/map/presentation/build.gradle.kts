plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
    kotlin("kapt")
}

android {
    namespace = "org.codingforanimals.map.presentation"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:datastore"))
    implementation(libs.compose)
    debugImplementation(libs.composeDebug)
    implementation(libs.daggerHilt)
    implementation(libs.daggerHiltCompose)
    kapt(libs.daggerHiltAndroidCompiler)
    //testing compose google maps api
    implementation("com.google.maps.android:maps-compose:2.8.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation(libs.koinAndroid)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}