@file:Suppress("ClassName")

object versions {
    const val core = "1.9.23"
    const val lifecycle = "2.5.1"
    const val coroutines = "1.6.4"
    const val compose = "1.3.1"
    const val composeCompiler = "1.5.11"
    const val accompanist = "0.25.1"
    const val retrofit = "2.9.0"
    const val moshi = "1.14.0"
    const val room = "2.6.1"
    const val mockk = "1.12.5"
    const val datastore = "1.0.0"
    const val koinAndroid = "3.3.2"
    const val koinAndroidCompose = "3.4.1"
    const val splashScreen = "1.0.1"
    const val paging = "3.3.0"
    const val paging_compose = "3.3.0"
}

object Firebase {
    const val BOM = "com.google.firebase:firebase-bom:32.1.1"
    const val FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
    const val REALTIME_DATABASE = "com.google.firebase:firebase-database-ktx"
    const val STORAGE = "com.google.firebase:firebase-storage-ktx"
    const val ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
    const val FUNCTIONS = "com.google.firebase:firebase-functions-ktx"
    const val AUTH = "com.google.firebase:firebase-auth-ktx"
    const val GEO_FIRE = "com.firebase:geofire-android-common:3.2.0"
}

object Other {
    const val IMAGE_CROPPER = "com.vanniktech:android-image-cropper:4.5.0"
    const val SHIMMER = "com.valentinilk.shimmer:compose-shimmer:1.0.5"
}

object Koin {
    const val koinAndroid = "io.insert-koin:koin-android:${versions.koinAndroid}"
    const val koinAndroidCompose =
        "io.insert-koin:koin-androidx-compose:${versions.koinAndroidCompose}"
    val libs = listOf(koinAndroid, koinAndroidCompose)
}

object libs {
    object google {
        const val gson = "com.google.code.gson:gson:2.10.1"
        const val auth = "com.google.android.gms:play-services-auth:20.5.0"
        const val places = "com.google.android.libraries.places:places:3.2.0"

        const val mapsUtils = "com.google.maps.android:android-maps-utils:2.3.0"
        const val playServicesMaps = "com.google.android.gms:play-services-maps:19.0.0"
        const val playServicesLocation = "com.google.android.gms:play-services-location:21.0.1"
        const val mapsCompose = "com.google.maps.android:maps-compose:6.1.1"
        val mapsLibs = listOf(
            mapsCompose,
            mapsUtils,
            playServicesMaps,
            playServicesLocation,
        )

        const val placeholder = "com.google.accompanist:accompanist-placeholder-material:0.29.1-alpha"
    }

    object firebase {
        const val bom = "com.google.firebase:firebase-bom:32.1.1"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val storage = "com.google.firebase:firebase-storage-ktx"
        const val database = "com.google.firebase:firebase-database-ktx"
        const val geofire = "com.firebase:geofire-android:3.2.0"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    }


    const val coil = "io.coil-kt:coil-compose:2.7.0"

    object androidx {
        const val composeBom = "2024.06.00"
        val compose = listOf(
            "androidx.compose.ui:ui",
            "androidx.compose.material3:material3",
            "androidx.compose.material:material-icons-extended",
            "androidx.compose.ui:ui-tooling-preview",
//        "androidx.activity:activity-compose:1.6.1",
            "androidx.navigation:navigation-compose:2.7.7",
            "androidx.lifecycle:lifecycle-runtime-compose:2.8.4",
        )

        const val datastore = "androidx.datastore:datastore-preferences:${versions.datastore}"
        const val paging = "androidx.paging:paging-runtime-ktx:${versions.paging}"
        const val paging_compose = "androidx.paging:paging-compose:${versions.paging_compose}"
        const val room = "androidx.room:room-runtime:${versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${versions.room}"
        const val roomRuntimeCompiler = "androidx.room:room-compiler:${versions.room}"
    }

    val android = listOf(
        "androidx.core:core-ktx:${versions.core}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${versions.lifecycle}",
    )

    val composeDebug = listOf(
        "androidx.compose.ui:ui-tooling",
        "androidx.compose.ui:ui-test-manifest"
    )

    val googleMapCompose = listOf(
        "com.google.maps.android:maps-compose:2.8.0",
        "com.google.maps.android:android-maps-utils:2.3.0",
        "com.google.android.gms:play-services-maps:18.1.0",
        "com.google.android.gms:play-services-location:21.0.1",
    )

    const val placeholder = "com.google.accompanist:accompanist-placeholder-material:0.29.1-alpha"

    const val permissionsCompose =
        "com.google.accompanist:accompanist-permissions:${versions.accompanist}"

    const val swipeRefreshCompose =
        "com.google.accompanist:accompanist-swiperefresh:${versions.accompanist}"

    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.coroutines}"

    val unitTests = listOf(
        "junit:junit:4.13.2",
        "io.mockk:mockk:${versions.mockk}",
        "io.mockk:mockk-agent-jvm:${versions.mockk}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines}"
    )
    val androidTests = listOf(
        "androidx.test.ext:junit:1.1.3",
        "androidx.test.espresso:espresso-core:3.4.0",
        "androidx.compose.ui:ui-test-junit4"
    )
    const val uiautomator =
        "androidx.test.uiautomator:uiautomator:2.2.0"

    const val macroBenchmark =
        "androidx.benchmark:benchmark-macro-junit4:1.1.0-beta04"

    const val profileInstaller =
        "androidx.profileinstaller:profileinstaller:1.2.0"
}