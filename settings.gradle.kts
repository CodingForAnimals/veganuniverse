pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "veganuniverse"
include(":app")

// core
include(":core:datastore")
include(":core:common")
include(":core:ui")

// features
include(":feature:profile:presentation")

include(":feature:community:presentation")
include(":feature:community:model")
include(":feature:community:data")

include(":feature:featuredtopic:presentation")
include(":feature:featuredtopic:data")
include(":feature:featuredtopic:model")

include(":feature:onboarding:presentation")

include(":feature:recipes:presentation")

include(":feature:post:presentation")

include(":feature:map:presentation")

include(":feature:site:presentation")

include(":feature:places:presentation")

include(":feature:registration:presentation")
include(":feature:onboarding:data")
include(":feature:onboarding:model")

include(":feature:notifications:presentation")

include(":feature:create:presentation")

include(":feature:search:presentation")

include(":feature:settings:presentation")
include(":firebase")
include(":feature:create:domain")
include(":feature:places:domain")
include(":core:location")
