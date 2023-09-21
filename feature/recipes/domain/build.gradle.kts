plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.domain"
}

dependencies {
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Services.Firebase.RECIPES))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}