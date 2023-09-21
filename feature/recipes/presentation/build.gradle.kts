plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.presentation"

}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Feature.Recipes.DOMAIN))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}