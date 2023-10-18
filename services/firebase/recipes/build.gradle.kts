plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.api"
}

dependencies {
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Entity.BASE))
    implementation(project(Module.Services.Firebase.BASE))
    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.REALTIME_DATABASE,
            Firebase.FIRESTORE,
            Firebase.STORAGE,
        )
    )
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}