@file:Suppress("ClassName")

object versions {
    const val core = "1.8.0"
    const val lifecycle = "2.5.1"
    const val coroutines = "1.6.4"
//    const val composeBom = "2023.02.00"
    const val compose = "1.3.1"
    const val composeCompiler = "1.4.0"
    const val accompanist = "0.25.1"
    const val retrofit = "2.9.0"
    const val moshi = "1.14.0"
    const val room = "2.4.3"
    const val mockk = "1.12.5"
    const val material3 = "1.1.0-alpha03"
    const val datastore = "1.0.0"
    const val koinAndroid = "3.3.2"
    const val koinAndroidCompose = "3.4.1"
    const val splashScreen = "1.0.0"
    const val googleMapCompose = "2.8.0"
    const val googleMapServices = "18.1.0"
}

object libs {
    val android = listOf(
        "androidx.core:core-ktx:${versions.core}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${versions.lifecycle}"
    )
    val compose = listOf(
        "androidx.compose.ui:ui:${versions.compose}",
        "androidx.compose.material3:material3:${versions.material3}",
        "androidx.compose.material:material-icons-extended:${versions.compose}",
        "androidx.compose.ui:ui-tooling-preview:${versions.compose}",
        "androidx.activity:activity-compose:1.6.1",
        "androidx.navigation:navigation-compose:2.5.1",
        "androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha04",
        "io.coil-kt:coil-compose:2.2.0"
    )

    val composeDebug = listOf(
        "androidx.compose.ui:ui-tooling:${versions.compose}",
        "androidx.compose.ui:ui-test-manifest:${versions.compose}"
    )

    const val placeholder = "com.google.accompanist:accompanist-placeholder-material:0.29.1-alpha"

    const val preferencesDatastore = "androidx.datastore:datastore-preferences:${versions.datastore}"

    const val permissionsCompose =
        "com.google.accompanist:accompanist-permissions:${versions.accompanist}"

    const val swipeRefreshCompose =
        "com.google.accompanist:accompanist-swiperefresh:${versions.accompanist}"

    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.coroutines}"

    const val koinAndroid = "io.insert-koin:koin-android:${versions.koinAndroid}"
    const val koinAndroidCompose = "io.insert-koin:koin-androidx-compose:${versions.koinAndroidCompose}"

    val retrofit = listOf(
        "com.squareup.retrofit2:retrofit:${versions.retrofit}",
        "com.squareup.retrofit2:converter-moshi:${versions.retrofit}",
        "com.squareup.okhttp3:logging-interceptor:4.10.0",
        "com.squareup.moshi:moshi-kotlin:${versions.moshi}",
        "com.squareup.moshi:moshi-adapters:${versions.moshi}",
    )
    val room = listOf(
        "androidx.room:room-ktx:${versions.room}",
        "androidx.room:room-runtime:${versions.room}",
        "androidx.room:room-paging:2.5.0-alpha01",
    )
    const val roomCompiler =
        "androidx.room:room-compiler:${versions.room}"

    val unitTests = listOf(
        "junit:junit:4.13.2",
        "io.mockk:mockk:${versions.mockk}",
        "io.mockk:mockk-agent-jvm:${versions.mockk}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}"
    )
    val androidTests = listOf(
        "androidx.test.ext:junit:1.1.3",
        "androidx.test.espresso:espresso-core:3.4.0",
        "androidx.compose.ui:ui-test-junit4:${versions.compose}"
    )
    const val uiautomator =
        "androidx.test.uiautomator:uiautomator:2.2.0"

    const val macroBenchmark =
        "androidx.benchmark:benchmark-macro-junit4:1.1.0-beta04"

    const val profileInstaller =
        "androidx.profileinstaller:profileinstaller:1.2.0"
}