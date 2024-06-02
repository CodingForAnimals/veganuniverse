plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.place.domain"
}

dependencies {
    implementation(project(":commons:place:domain"))
    implementation(project(":commons:network"))
    implementation(project(":commons:user:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:location"))
    implementation(project(":core:auth"))
    implementation(project(":entity:places"))
    implementation(project(":services:firebase:places"))
    implementation(project(":services:firebase:profile"))
    implementation(project(":model:profile"))
}
