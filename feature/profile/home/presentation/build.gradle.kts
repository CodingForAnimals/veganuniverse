plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.profile.home.presentation"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:auth"))
    implementation(project(":feature:profile:home:domain"))
    implementation(project(":model:profile"))
    implementation(project(":entity:places"))
    implementation(project(":entity:recipes"))
    implementation(project(":ui:places"))
    implementation(project(":ui"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}