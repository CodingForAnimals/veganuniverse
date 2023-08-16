plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.places.domain"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Services.Firebase.PLACES))
    implementation(project(Module.Services.Firebase.BASE))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}