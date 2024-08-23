plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.profile.domain"
}

dependencies {
    implementation(project(":commons:profile:data"))
    implementation(project(":commons:profile:shared"))
    implementation(project(":commons:user:domain"))
}
