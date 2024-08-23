plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.create.product.data"
}

dependencies {
    bomImplementation(Firebase.BOM, listOf(Firebase.FIRESTORE, Firebase.STORAGE))
    implementation(project(":services:firebase"))
}