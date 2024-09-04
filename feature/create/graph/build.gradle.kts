plugins {
    id("com.android.library")
    id("kotlin-android")
    `android-config`
    `compose-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.graph"
}

dependencies {
    implementation(project(":commons:navigation"))
    implementation(project(":commons-create:domain"))
    implementation(project(":feature:create:home:presentation"))
    implementation(project(":feature:create:place:presentation"))
    implementation(project(":feature:create:recipe:presentation"))
    implementation(project(":feature:create:product:presentation"))
    implementation(project(":feature:create:thank_you:presentation"))
}