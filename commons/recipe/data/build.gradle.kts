plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipe.data"
}

dependencies {
    implementation(project(":commons:firebase:storage"))
    implementation(project(":commons:recipe:shared"))
    implementation(libs.google.gson)
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = Firebase.BOM,
        dependencies = listOf(
            Firebase.FIRESTORE,
        )
    )
    implementation(libs.room)
    ksp(libs.roomCompiler)
}
