plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.presentation"

}

dependencies {
    implementation(project(":feature:recipe:domain"))
    implementation(project(":commons:recipe:domain"))
    implementation(project(":commons:recipe:presentation"))
    implementation(project(":core:common"))
    implementation(project(":core:auth"))
    implementation(libs.androidx.paging_compose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}