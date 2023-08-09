plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":shared:entity"))
    implementation(project(":feature:create:domain"))
    implementation(libs.googleMapCompose)
    implementation(libs.imageCropper)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // testing geofire
    implementation("com.firebase:geofire-android-common:3.2.0")

    implementation("com.google.android.libraries.places:places:3.1.0")
}