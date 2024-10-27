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
    ":feature:onboarding:domain",
    ":feature:onboarding:presentation"
)

include(
    ":feature:registration:presentation"
)

include(
    ":feature:create:presentation",
    ":feature:create:home:presentation",
)

include(
    ":services:auth",
    ":services:location",
    ":services:google-places",
    ":services:firebase-storage"
)

include(
    ":feature:place:shared",
    ":feature:place:data",
    ":feature:place:domain",
    ":feature:place:presentation",
)

include(
    ":feature:recipe:shared",
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
    ":feature:additives:data",
    ":feature:additives:domain",
    ":feature:additives:presentation",
)

include(
    ":commons:user:data",
    ":commons:user:domain",
    ":commons:user:presentation",
)

include(
    ":commons:analytics"
)

include(
    ":commons-profile:data",
    ":commons-profile:domain",
)

include(
//    ":commons-recipe:shared",
//    ":commons-recipe:data",
//    ":commons-recipe:domain",
//    ":commons-recipe:presentation",
)

include(
//    ":commons-product:shared",
//    ":commons-product:data",
//    ":commons-product:domain",
//    ":commons-product:presentation",
)

include(
)

include(
    ":commons:ui",
    ":commons:designsystem",
    ":commons:navigation",
    ":commons:data",
)
