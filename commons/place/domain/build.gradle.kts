plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.place.domain"
}

dependencies {
    implementation(project(":commons:place:data"))
    api(project(":commons:place:shared"))
    implementation(libs.androidx.paging)
}
