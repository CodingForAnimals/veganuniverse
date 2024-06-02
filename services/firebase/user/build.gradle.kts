plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.user.services.firebase"
}

dependencies {
    implementation(project(":services:firebase"))
    implementation(project(":entity"))

    bomImplementation(
        Firebase.BOM,
        listOf(Firebase.AUTH, Firebase.STORAGE, Firebase.REALTIME_DATABASE, Firebase.FIRESTORE)
    )
    implementation(Google.AUTH)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}