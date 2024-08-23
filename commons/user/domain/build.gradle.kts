plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.user.domain"
}

dependencies {
    implementation(project(":commons:user:data"))
    implementation(project(":commons:navigation"))
    implementation(project(":services:auth"))
}
