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
    implementation(project(":commons:place:shared"))
    implementation(project(":commons:place:presentation"))
    implementation(project(":core:common"))
    implementation(project(":core:location"))
    implementation(project(":core:auth"))
    implementation(project(":feature:create:place:domain"))
    implementation(project(":entity:places"))
    implementation(project(":ui:places"))
    implementation(project(":ui"))
    implementation(project(":ui:create"))
    implementation(project(":services:google:places"))
    implementation(Google.MAPS)

}