plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.domain"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Entity.BASE))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Services.Firebase.PLACES))
    implementation(project(Module.Services.Firebase.RECIPES))
    implementation(project(Module.Services.Firebase.PROFILE))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}