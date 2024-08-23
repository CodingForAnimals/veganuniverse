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
    implementation(project(":commons:user:presentation"))
    implementation(project(":commons-create:presentation"))
    implementation(project(":commons-place:shared"))
    implementation(project(":commons:navigation"))
    implementation(project(":commons-place:presentation"))
    implementation(project(":feature:create:place:domain"))
    implementation(project(":services:google-places"))
    implementation(project(":services:location"))
    implementation(Google.MAPS)

}