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
    implementation(project(":feature:recipe:shared"))
    implementation(project(":feature:recipe:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons:navigation"))
    implementation(libs.androidx.paging_compose)
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}