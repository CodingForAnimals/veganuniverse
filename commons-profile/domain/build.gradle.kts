plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.profile.domain"
}

dependencies {
    implementation(project(":commons-profile:data"))
    api(project(":commons-profile:shared"))
    implementation(project(":commons:user:domain"))
}
