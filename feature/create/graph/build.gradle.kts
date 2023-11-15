plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.graph"
}

dependencies {
    implementation(project(Module.Create.Home.PRESENTATION))
    implementation(project(Module.Create.Place.PRESENTATION))
    implementation(project(Module.Create.Recipe.PRESENTATION))
    implementation(project(Module.Create.ThankYou.PRESENTATION))
}