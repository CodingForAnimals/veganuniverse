plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.onboarding.presentation"
}

dependencies {
    implementation(libs.compose)
//    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    debugImplementation(libs.composeDebug)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}