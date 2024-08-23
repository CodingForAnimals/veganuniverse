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

include(":feature:onboarding:presentation")

include(":services:firebase:places")

include(":entity:places")
include(":entity:recipes")

include(":feature:registration:presentation")

include(":feature:onboarding:data")
include(":feature:onboarding:model")

include(":feature:create:graph")
include(":feature:create:home:presentation")
include(
    ":feature:create:place:domain",
    ":feature:create:place:presentation",
)

include(
    ":feature:create:recipe:domain",
    ":feature:create:recipe:presentation",
)

include(
    ":feature:create:thank_you:presentation",
)

include(
    ":feature:settings:presentation",
)

include(":core:auth")
include(":services:firebase:user")

include(":services:firebase")
include(":services:google:places")
include(":services:firebase:recipes")

include(":storage:firestore")
include(":storage:recipes")

include(":services:firebase:profile")

include(":ui:places")
include("ui:recipes")
include("ui:create")
include("ui:product")
include("model:profile")

include(
    ":feature:place:domain",
    ":feature:place:presentation",
)

include(
    ":feature:create:product:data",
    ":feature:create:product:domain",
    ":feature:create:product:presentation"
)

include(
    ":feature:recipe:data",
    ":feature:recipe:domain",
    ":feature:recipe:presentation",
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

include(
    ":commons:profile:shared",
    ":commons:profile:data",
    ":commons:profile:domain",
)

include(
    ":commons:recipe:shared",
    ":commons:recipe:data",
    ":commons:recipe:domain",
    ":commons:recipe:presentation",
)

include(
    ":commons:product:shared",
    ":commons:product:data",
    ":commons:product:domain",
    ":commons:product:presentation",
)

include(
    ":commons:place:shared",
    ":commons:place:data",
    ":commons:place:domain",
    ":commons:place:presentation",
)

include(
    ":commons:network",
)