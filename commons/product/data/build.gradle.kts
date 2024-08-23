plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.data"
}

dependencies {
    implementation(project(":commons:product:shared"))
    implementation(project(":commons:firebase:storage"))
    implementation(libs.androidx.paging)
    bomImplementation(
        bom = Firebase.BOM,
        dependencies = listOf(
            Firebase.FIRESTORE,
        )
    )
}
