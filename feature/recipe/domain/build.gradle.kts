plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.domain"
}

dependencies {
    implementation(project(":feature:recipe:shared"))
    implementation(project(":feature:recipe:data"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons-profile:domain"))
    implementation(libs.androidx.paging)
}