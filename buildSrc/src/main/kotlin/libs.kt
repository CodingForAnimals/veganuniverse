@file:Suppress("ClassName")

object versions {
    const val core = "1.9.0"
    const val lifecycle = "2.5.1"
    const val coroutines = "1.6.4"
    const val composeBom = "2023.10.01"
    const val compose = "1.3.1"
    const val composeCompiler = "1.5.1"
    const val accompanist = "0.25.1"
    const val retrofit = "2.9.0"
    const val moshi = "1.14.0"
    const val room = "2.4.3"
    const val mockk = "1.12.5"
    const val datastore = "1.0.0"
    const val koinAndroid = "3.3.2"
    const val koinAndroidCompose = "3.4.1"
    const val splashScreen = "1.0.1"
    const val googleMapCompose = "2.8.0"
    const val googleMapServices = "18.1.0"
}

object Module {
    const val UI = ":ui"

    object Shared {
        const val UI = ":shared:ui"
    }

    object Storage {
        const val FIRESTORE = ":storage:firestore"
        const val RECIPES = ":storage:recipes"
    }

    object Core {
        const val COMMON = ":core:common"
        const val DATASTORE = ":core:datastore"
        const val UI = ":core:ui"
        const val LOCATION = ":core:location"
        const val AUTH = ":core:auth"
    }

    object Entity {
        const val BASE = ":entity"
        const val PLACES = ":entity:places"
        const val RECIPES = ":entity:recipes"
        const val PRODUCT = ":entity:product"
    }

    object Services {
        object Firebase {
            const val BASE = ":services:firebase"
            const val USER = ":services:firebase:user"
            const val PROFILE = ":services:firebase:profile"
            const val PLACES = ":services:firebase:places"
            const val RECIPES = ":services:firebase:recipes"
        }

        object Google {
            const val PLACES = ":services:google:places"
        }
    }

    object Places {
        const val DOMAIN = ":feature:places:domain"
        const val PRESENTATION = ":feature:places:presentation"
        const val UI = ":ui:places"
        const val ENTITY = ":entity:places"

        object Services {
            const val FIREBASE = ":services:firebase:places"
        }
    }

    object Product {
        const val GRAPH = ":feature:product:graph"

        object Categories {
            const val DOMAIN = ":feature:product:categories:domain"
            const val PRESENTATION = ":feature:product:categories:presentation"
        }

        object List {
            const val DOMAIN = ":feature:product:list:domain"
            const val PRESENTATION = ":feature:product:list:presentation"
        }

        const val UI = ":ui:product"

        object Services {
            const val FIREBASE = ":services:firebase:product"
        }
    }

    object Profile {
        const val GRAPH = ":feature:profile"
        const val MODEL = ":model:profile"

        object Home {
            const val DOMAIN = ":feature:profile:home:domain"
            const val PRESENTATION = ":feature:profile:home:presentation"
        }

        object ItemList {
            const val DOMAIN = ":feature:profile:itemlist:domain"
            const val PRESENTATION = ":feature:profile:itemlist:presentation"
        }

        object Services {
            const val FIREBASE = ":services:firebase:profile"
        }
    }

    object Create {
        const val UI = ":ui:create"
        const val GRAPH = ":feature:create:graph"

        object Home {
            const val PRESENTATION = ":feature:create:home:presentation"
        }

        object Place {
            const val DOMAIN = ":feature:create:place:domain"
            const val PRESENTATION = ":feature:create:place:presentation"
        }

        object Recipe {
            const val DOMAIN = ":feature:create:recipe:domain"
            const val PRESENTATION = ":feature:create:recipe:presentation"
        }

        object Product {
            const val DOMAIN = ":feature:create:product:domain"
            const val PRESENTATION = ":feature:create:product:presentation"
        }

        object ThankYou {
            const val PRESENTATION = ":feature:create:thank_you:presentation"
        }
    }

    object Recipes {
        const val DOMAIN = ":feature:recipes:domain"
        const val PRESENTATION = ":feature:recipes:presentation"
        const val UI = ":ui:recipes"
    }
}

object Android {
    const val DATASTORE = "androidx.datastore:datastore-preferences:${versions.datastore}"
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

object Google {
    const val AUTH = "com.google.android.gms:play-services-auth:20.5.0"
    const val GSON = "com.google.code.gson:gson:2.10.1"
    const val PLACES = "com.google.android.libraries.places:places:3.2.0"

    const val MAPS_COMPOSE = "com.google.maps.android:maps-compose:2.8.0"
    const val MAPS_UTILS = "com.google.maps.android:android-maps-utils:2.3.0"
    const val PLAY_SERVICES_MAPS = "com.google.android.gms:play-services-maps:18.1.0"
    const val PLAY_SERVICES_LOCATION = "com.google.android.gms:play-services-location:21.0.1"
    val MAPS = listOf(
        MAPS_COMPOSE,
        MAPS_UTILS,
        PLAY_SERVICES_MAPS,
        PLAY_SERVICES_LOCATION,
    )

    const val PLACEHOLDER = "com.google.accompanist:accompanist-placeholder-material:0.29.1-alpha"
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

    val android = listOf(
        "androidx.core:core-ktx:${versions.core}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${versions.lifecycle}",
    )
    val compose = listOf(
        "androidx.compose.ui:ui",
        "androidx.compose.material3:material3",
        "androidx.compose.material:material-icons-extended",
        "androidx.compose.ui:ui-tooling-preview",
        "androidx.activity:activity-compose:1.6.1",
        "androidx.navigation:navigation-compose:2.5.2",
        "androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha04",
        "io.coil-kt:coil-compose:2.2.0"
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
        "androidx.compose.ui:ui-test-junit4"
    )
    const val uiautomator =
        "androidx.test.uiautomator:uiautomator:2.2.0"

    const val macroBenchmark =
        "androidx.benchmark:benchmark-macro-junit4:1.1.0-beta04"

    const val profileInstaller =
        "androidx.profileinstaller:profileinstaller:1.2.0"
}