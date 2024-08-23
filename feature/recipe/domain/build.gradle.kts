plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.recipes.domain"
}

dependencies {
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:recipe:domain"))
    implementation(project(":commons:profile:domain"))
    api(project(":commons:profile:shared"))
    implementation(project(":entity:recipes"))
    implementation(project(":services:firebase:recipes"))
    implementation(project(":services:firebase:profile"))
    implementation(project(":storage:recipes"))
    implementation(project(":model:profile"))
}