plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":shared:entity"))
    bomImplementation(
        Firebase.BOM,
        listOf(Firebase.FIRESTORE, Firebase.ANALYTICS)
    )
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // testing geofire
    implementation("com.firebase:geofire-android-common:3.2.0")
}