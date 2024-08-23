plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.create.recipe.domain"
}

dependencies {
    implementation(project(":commons:network"))
    implementation(project(":commons:user:domain"))
    implementation(project(":commons:recipe:domain"))
    implementation(project(":commons:profile:domain"))
    implementation(project(":entity:recipes"))
    implementation(project(":services:firebase:recipes"))
    implementation(project(":services:firebase:profile"))
    implementation(project(":model:profile"))
}
