rootProject.name = "Kluvaka"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:presentation")
include(":core:domain")
include(":core:data")
include(":feature:auth:presentation")
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:session:domain")
include(":feature:session:data")
include(":feature:session:presentation")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:equipment:domain")
include(":feature:equipment:data")
include(":feature:equipment:presentation")
include(":feature:more:presentation")
include(":feature:more:data")
include(":feature:more:domain")
