plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.product.domain"
}

dependencies {
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-product:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(project(":commons:network"))
}
