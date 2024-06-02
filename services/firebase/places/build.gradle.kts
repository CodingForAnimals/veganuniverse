plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.places.services.firebase"
}

dependencies {
    implementation(project(":services:firebase"))
    implementation(project(":entity"))
    implementation(project(":entity:places"))
    implementation(project(":core:common"))


    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.FIRESTORE,
            Firebase.ANALYTICS,
            Firebase.GEO_FIRE,
            Firebase.REALTIME_DATABASE,
            Firebase.STORAGE,
        )
    )
    implementation("com.firebase:geofire-android:3.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}