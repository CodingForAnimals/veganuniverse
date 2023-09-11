plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.places.presentation"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.UI))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Core.LOCATION))
    implementation(project(Module.Services.Google.PLACES))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Feature.Places.DOMAIN))
    implementation(Google.MAPS)


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}