plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.presentation"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.LOCATION))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Create.DOMAIN))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Places.UI))
    implementation(project(Module.Recipes.UI))
    implementation(project(Module.UI))
    implementation(project(Module.Entity.RECIPES))
    implementation(project(Module.Services.Google.PLACES))
    implementation(Google.MAPS)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}