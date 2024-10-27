plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.additives.domain"
}

dependencies {
    implementation(project(":feature:additives:data"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:data"))
}
