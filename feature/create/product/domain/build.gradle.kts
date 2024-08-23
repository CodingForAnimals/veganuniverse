plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.product.domain"
}

dependencies {
    implementation(project(Module.Product.STORAGE))
    implementation(project(Module.Product.ENTITY))
    implementation(project(Module.Product.Services.FIREBASE))
    implementation(project(Module.Services.Firebase.PROFILE))
    implementation(project(Module.Profile.MODEL))
}
