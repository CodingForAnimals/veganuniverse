plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.place.presentation"
}

dependencies {
    api(project(":commons-place:domain"))
    api(project(":commons-place:shared"))
}
