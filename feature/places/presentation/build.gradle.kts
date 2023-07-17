plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.places.presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:auth"))
    implementation(project(":core:location"))
    implementation(project(":feature:places:domain"))
    implementation(libs.googleMapCompose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}