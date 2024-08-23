plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.place.data"
}

dependencies {
    implementation(project(":commons:place:shared"))
    implementation(project(":commons:firebase:storage"))
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.firestore,
            libs.firebase.database,
            libs.firebase.geofire,
        )
    )
}
