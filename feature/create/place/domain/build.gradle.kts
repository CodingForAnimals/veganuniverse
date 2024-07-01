plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.place.domain"
}

dependencies {
    implementation(project(":commons-place:domain"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:navigation"))
    implementation(project(":commons:network"))
}
