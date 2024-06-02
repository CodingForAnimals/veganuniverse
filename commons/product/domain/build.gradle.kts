plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.product.domain"
}

dependencies {
    implementation(project(":commons:product:data"))
    api(project(":commons:product:shared"))
    implementation(libs.androidx.paging)
}
