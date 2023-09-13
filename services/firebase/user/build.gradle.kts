plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.auth.services.firebase"
}

dependencies {
    implementation(project(Module.Services.Firebase.BASE))
    implementation(project(Module.Entity.BASE))

    bomImplementation(
        Firebase.BOM,
        listOf(Firebase.AUTH, Firebase.STORAGE)
    )
    implementation(Google.AUTH)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}