plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.services.firebase.places"
}

dependencies {
    implementation(project(Module.Services.Firebase.BASE))
    implementation(project(Module.Entity.BASE))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Core.COMMON))


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