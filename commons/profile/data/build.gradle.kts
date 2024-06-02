plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.profile.data"
}

dependencies {
    bomImplementation(
        bom = Firebase.BOM,
        dependencies = listOf(
            Firebase.REALTIME_DATABASE,
        )
    )
    implementation(project(":services:firebase"))
    implementation(libs.room)
    ksp(libs.roomCompiler)
}
