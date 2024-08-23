plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.place.domain"
}

dependencies {
    implementation(project(":commons-place:domain"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(project(":services:google-places"))
    implementation(project(":commons:navigation"))

    implementation(libs.androidx.paging)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}