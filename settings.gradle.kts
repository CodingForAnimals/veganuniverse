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

include(
    ":feature:profile"
)

include(
    ":feature:onboarding:data",
    ":feature:onboarding:model",
    ":feature:onboarding:presentation"
)

include(
    ":feature:registration:presentation"
)

include(
    ":feature:create:graph",
    ":feature:create:home:presentation",
    ":feature:create:place:domain",
    ":feature:create:place:presentation",
    ":feature:create:recipe:domain",
    ":feature:create:recipe:presentation",
    ":feature:create:product:domain",
    ":feature:create:product:presentation",
    ":feature:create:thank_you:presentation",
)

include(
    ":services:auth",
    ":services:location",
    ":services:google-places",
    ":services:firebase-storage"
)

include(
    ":feature:place:domain",
    ":feature:place:presentation",
)

include(
    ":feature:recipe:data",
    ":feature:recipe:domain",
    ":feature:recipe:presentation",
)

include(
    ":feature:product:domain",
    ":feature:product:presentation"
)

include(
    ":commons:user:data",
    ":commons:user:domain",
    ":commons:user:presentation",
)

include(
    ":commons-profile:shared",
    ":commons-profile:data",
    ":commons-profile:domain",
)

include(
    ":commons-recipe:shared",
    ":commons-recipe:data",
    ":commons-recipe:domain",
    ":commons-recipe:presentation",
)

include(
    ":commons-product:shared",
    ":commons-product:data",
    ":commons-product:domain",
    ":commons-product:presentation",
)

include(
    ":commons-place:shared",
    ":commons-place:data",
    ":commons-place:domain",
    ":commons-place:presentation",
)

include(
    ":commons:ui",
    ":commons:designsystem",
    ":commons:network",
    ":commons:navigation",
    ":commons:data",
)

include(
    ":commons-create:presentation",
)
