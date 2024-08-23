plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.product.data"
}

dependencies {
    implementation(project(":commons-product:shared"))
    implementation(project(":services:firebase-storage"))
    implementation(project(":commons:network"))
    implementation(project(":commons:data"))
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = libs.firebase.bom,
        dependencies = listOf(
            libs.firebase.firestore,
            libs.firebase.database,
        )
    )
}
