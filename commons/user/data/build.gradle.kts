plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.user.data"
}

dependencies {
    bomImplementation(
        bom = Firebase.BOM,
        dependencies = listOf(
            Firebase.FIRESTORE,
        )
    )
    implementation(project(Module.Services.Firebase.BASE))
}