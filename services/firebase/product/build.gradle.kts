plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.services.firebase"
}

dependencies {
    implementation(project(Module.Product.ENTITY))
    implementation(project(Module.Entity.BASE))
    implementation(project(Module.Services.Firebase.BASE))
    implementation(project(Module.Storage.FIRESTORE))
    bomImplementation(
        Firebase.BOM,
        listOf(
            Firebase.REALTIME_DATABASE,
            Firebase.FIRESTORE,
            Firebase.STORAGE,
        )
    )
}