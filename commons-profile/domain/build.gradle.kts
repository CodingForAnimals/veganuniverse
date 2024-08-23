plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.profile.domain"
}

dependencies {
    implementation(project(":commons-profile:data"))
    implementation(project(":commons:user:domain"))
}
