plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.place.domain"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(Module.Core.LOCATION))
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Places.ENTITY))
    implementation(project(Module.Places.Services.FIREBASE))
    implementation(project(Module.Profile.Services.FIREBASE))
    implementation(project(Module.Profile.MODEL))
}
