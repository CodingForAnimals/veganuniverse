plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.data"
}

dependencies {
    implementation(project(Module.Core.COMMON))
    implementation(project(":commons:firebase:storage"))
    implementation(AndroidX.paging)
    implementation(AndroidX.datastore)

    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.FIRESTORE,
            Firebase.ANALYTICS,
            Firebase.AUTH,
            Firebase.STORAGE,
        )
    )
    implementation(project(Module.Core.AUTH))
    implementation(project(Module.Services.Firebase.BASE))
}
