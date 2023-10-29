plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.profile.services.firebase"
}

dependencies {
    implementation(project(Module.Services.Firebase.BASE))
    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.REALTIME_DATABASE
        )
    )
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}