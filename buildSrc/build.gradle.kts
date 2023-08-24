plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}
repositories {
    google()
    mavenCentral()
}


dependencies {
    implementation("com.android.tools.build:gradle:8.1.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
}