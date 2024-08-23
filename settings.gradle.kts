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
include(":entity:recipes")

include(":feature:registration:presentation")

include(":feature:onboarding:data")
include(":feature:onboarding:model")

include(":feature:notifications:presentation")

include(":feature:create:graph")
include(":feature:create:home:presentation")
include(":feature:create:place:domain")
include(":feature:create:place:presentation")
include(":feature:create:recipe:domain")
include(":feature:create:recipe:presentation")
include(":feature:create:thank_you:presentation")

include(":feature:search:presentation")

include(":feature:settings:presentation")

include(":core:auth")
include(":services:firebase:user")

include(":services:firebase")
include(":services:google:places")
include(":services:firebase:recipes")
include(":feature:recipes:domain")

include(":storage:firestore")
include(":storage:recipes")

include(":services:firebase:profile")

include(":ui:places")
include("ui:recipes")
include("ui:create")
include("ui:product")
include("model:profile")

include(
    ":feature:create:product:data",
    ":feature:create:product:domain",
    ":feature:create:product:presentation"
)

include(
    ":feature:product:data",
    ":feature:product:domain",
    ":feature:product:presentation"
)

include(
    ":commons:user:data",
    ":commons:user:domain",
    ":commons:firebase:storage",
)
