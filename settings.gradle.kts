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

include(":core:datastore")
include(":core:common")
include(":core:ui")
include(":core:location")

include(":feature:profile")
include(":feature:profile:home:domain")
include(":feature:profile:home:presentation")
include(":feature:profile:itemlist:domain")
include(":feature:profile:itemlist:presentation")

include(":feature:community:presentation")

include(":feature:featuredtopic:presentation")
include(":feature:featuredtopic:data")
include(":feature:featuredtopic:model")

include(":feature:onboarding:presentation")

include(":feature:recipes:presentation")

include(":feature:post:presentation")

include(":feature:places:presentation")
include(":feature:places:domain")
include(":services:firebase:places")
include(":entity:places")

include(":feature:registration:presentation")

include(":feature:onboarding:data")
include(":feature:onboarding:model")

include(":feature:notifications:presentation")

include(":feature:create:presentation")
include(":feature:create:domain")

include(":feature:search:presentation")

include(":feature:settings:presentation")

include(":core:auth")
include(":services:firebase:user")

include(":services:firebase")
include(":services:google:places")
include(":services:firebase:recipes")
include(":entity:recipes")
include(":feature:recipes:domain")

include(":storage:firestore")
include(":storage:recipes")

include(":services:firebase:profile")

include(":shared:ui")
include(":ui:places")
include("ui:recipes")
include("model:profile")