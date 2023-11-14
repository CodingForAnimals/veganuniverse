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
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Profile.Home.DOMAIN))
    implementation(project(Module.Profile.MODEL))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Shared.UI))
    implementation(project(Module.Places.UI))
    implementation(project(Module.UI))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}