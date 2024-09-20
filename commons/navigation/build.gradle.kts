plugins {
    id("com.android.library")
    `android-config`
}

android {
    namespace = "org.codingforanimals.veganuniverse.commons.navigation"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            buildConfigField(
                type = "String",
                name = "APP_LINKS_BASE_URL",
                value = "\"https://veganuniverse-prod.firebaseapp.com\""
            )
        }

        debug {
            buildConfigField(
                type = "String",
                name = "APP_LINKS_BASE_URL",
                value = "\"https://veganuniverse-a924e.firebaseapp.com\""
            )
        }
    }
}

dependencies {}
