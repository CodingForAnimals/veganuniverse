plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.place.presentation"
}

dependencies {
    implementation(project(":services:location"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-place:presentation"))
    implementation(project(":commons-place:domain"))
    implementation(project(":commons:navigation"))
    implementation(project(":feature:place:domain"))
    implementation(Google.MAPS)

    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging_compose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}