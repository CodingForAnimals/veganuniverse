plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.recipe.data"
}

dependencies {
    implementation(project(":services:firebase-storage"))
    implementation(project(":commons-recipe:shared"))
    implementation(project(":commons:data"))
    implementation(libs.google.gson)
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.firestore,
            libs.firebase.database,
        )
    )
}
