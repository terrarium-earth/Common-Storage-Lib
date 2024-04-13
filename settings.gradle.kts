enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "botarium"

pluginManagement {
    repositories {
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.neoforged.net/releases/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://maven.msrandom.net/repository/root")
        gradlePluginPortal()
    }
}

include("common")
include("fabric")
include("neoforge")