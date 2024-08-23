plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.product.domain"
}

dependencies {
    implementation(project(":feature:create:product:data"))
    implementation(project(Module.Core.AUTH))
}
