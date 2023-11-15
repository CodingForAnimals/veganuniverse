plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.place.presentation"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.LOCATION))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Create.Place.DOMAIN))
    implementation(project(Module.Entity.PLACES))
    implementation(project(Module.Places.UI))
    implementation(project(Module.UI))
    implementation(project(Module.Create.UI))
    implementation(project(Module.Services.Google.PLACES))
    implementation(Google.MAPS)

}