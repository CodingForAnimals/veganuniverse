plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.place.data"
}

dependencies {
    api(project(":commons:network"))
    implementation(project(":commons-place:shared"))
    implementation(project(":services:firebase-storage"))
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.firestore,
            libs.firebase.database,
            libs.firebase.geofire,
            libs.firebase.auth,
        )
    )
}
