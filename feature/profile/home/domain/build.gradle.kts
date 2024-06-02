plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.profile.home.domain"
}

dependencies {
    implementation(project(":services:firebase:places"))
    implementation(project(":services:firebase:user"))
    implementation(project(":services:firebase:profile"))
    implementation(project(":services:firebase:recipes"))
    implementation(project(":entity:places"))
    implementation(project(":entity:recipes"))
    implementation(project(":model:profile"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}