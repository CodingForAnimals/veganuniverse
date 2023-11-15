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
    implementation(project(Module.Services.Firebase.PROFILE))
    implementation(project(Module.Storage.RECIPES))
    implementation(project(Module.Profile.MODEL))
}